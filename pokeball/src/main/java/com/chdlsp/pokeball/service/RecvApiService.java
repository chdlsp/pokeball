package com.chdlsp.pokeball.service;

import com.chdlsp.pokeball.exception.RecvException;
import com.chdlsp.pokeball.model.entity.RecvInfo;
import com.chdlsp.pokeball.model.entity.SendInfo;
import com.chdlsp.pokeball.model.enumClass.RecvYnStatus;
import com.chdlsp.pokeball.model.network.response.RecvApiResponse;
import com.chdlsp.pokeball.repository.RecvInfoRepository;
import com.chdlsp.pokeball.repository.SendInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecvApiService {

    @Autowired
    SendInfoRepository sendInfoRepository;

    @Autowired
    RecvInfoRepository recvInfoRepository;

    public RecvApiResponse update(String xUserId, String xRoomId, String token) {

        RecvApiResponse recvApiService = new RecvApiResponse(); // return object

        Optional<List<RecvInfo>> recvInfoList = recvInfoRepository.findByxRoomIdAndTokenAndRecvYn(xRoomId, token, RecvYnStatus.NOT_RECEIVED);

        // 위에 조회한거 건 있으면 0번째 인덱스만 가져와서 update 처리
        return recvInfoList.map(data -> {

            LocalDateTime nowTime = LocalDateTime.now();

            // token 에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나 배분
            //  => 0번째 index 를 배분 처리 (update)
            RecvInfo recvInfo = RecvInfo.builder()
                    .id(data.get(0).getId())
                    .xUserId(data.get(0).getXUserId())
                    .xRoomId(data.get(0).getXRoomId())
                    .token(token)
                    .createdAt(data.get(0).getCreatedAt())
                    .recvSeq(data.get(0).getRecvSeq())
                    .recvAmt(data.get(0).getRecvAmt())
                    .recvUserId(xUserId)
                    .updatedAt(nowTime)
                    .recvYn(RecvYnStatus.RECEIVED) // 수령여부 Y
                    .build();

            RecvInfo updateRecvInfo = recvInfoRepository.save(recvInfo);

            recvApiService.setRecvAmt(updateRecvInfo.getRecvAmt());
            return recvApiService;

        }).orElseGet(()-> {
           throw RecvException.retrieveRecvInfoFailed();
        });
    }

    public boolean isAlreadyReceived(String xUserId, String xRoomId, String token) {
        Optional<RecvInfo> recvInfo = recvInfoRepository.findByxRoomIdAndTokenAndRecvUserIdAndRecvYn(xRoomId, token, xUserId, RecvYnStatus.RECEIVED);
        return recvInfo
                .map(data -> true)
                .orElseGet(() -> false);
    }

    // 뿌리기 정보에서 Http Header 의 X-USER-ID가 테이블에 저장된 X-USER-ID와 같으면 자신이 뿌린 건
    public boolean isSendFromItself(String xUserId, String xRoomId, String token) {
        Optional<SendInfo> sendInfo = sendInfoRepository.findByxRoomIdAndToken(xRoomId, token);
        return sendInfo
                .map(data -> {
                    if (data.getXUserId().equals(xUserId)) {
                        return true;
                    } else {
                        return false;
                    }
                }).orElseGet(() -> false);
    }

    // 주어진 토큰을 이용해 10분 이내 뿌리기 건 존재 여부 조회
    public boolean isValidSendInfoExists(String token) {
        Optional<SendInfo> sendInfo = sendInfoRepository.findByTokenAndCreatedAtAfter(token, LocalDateTime.now().minusMinutes(10));
        return sendInfo.map(data -> true).orElse(false);
    }
}
