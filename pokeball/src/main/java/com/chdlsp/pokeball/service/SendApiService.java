package com.chdlsp.pokeball.service;

import com.chdlsp.pokeball.controller.constant.KeyConstant;
import com.chdlsp.pokeball.exception.NotFoundException;
import com.chdlsp.pokeball.exception.SendException;
import com.chdlsp.pokeball.model.entity.RecvInfo;
import com.chdlsp.pokeball.model.entity.SendInfo;
import com.chdlsp.pokeball.model.enumClass.RecvYnStatus;
import com.chdlsp.pokeball.model.network.request.SendApiRequest;
import com.chdlsp.pokeball.model.network.response.SendApiResponse;
import com.chdlsp.pokeball.model.network.response.SendInfoResponse;
import com.chdlsp.pokeball.model.token.TokenGenerator;
import com.chdlsp.pokeball.model.vo.RecvUserInfoVO;
import com.chdlsp.pokeball.repository.RecvInfoRepository;
import com.chdlsp.pokeball.repository.SendInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SendApiService {

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    SendInfoRepository sendInfoRepository;

    @Autowired
    RecvInfoRepository recvInfoRepository;

    @Transactional
    public SendApiResponse create(String xUserId, String xRoomId, SendApiRequest ApiRequest) {

        LocalDateTime nowTime = LocalDateTime.now();
        SendInfo sendInfo = SendInfo.builder()
                .xUserId(xUserId)
                .xRoomId(xRoomId)
                .token(this.generateUniqueCode())
                .SendTotAmt(ApiRequest.getSendTotAmt())
                .SendTotCnt(ApiRequest.getSendTotCnt())
                .createdAt(nowTime)
                .build();

        SendInfo newSendInfo = sendInfoRepository.save(sendInfo);

        int totAmt = newSendInfo.getSendTotAmt().intValue();
        int totCnt = newSendInfo.getSendTotCnt();

        for (int i = 0; i < totCnt; i++) {

            // 분배 로직 : 100원씩 배분, 마지막 사람만 잔액 배분
            int recvAmtTmp = i == (totCnt - 1) ? totAmt - (100 * (totCnt - 1)) : 100;

            RecvInfo recvInfo = RecvInfo.builder()
                    .xUserId(xUserId)
                    .xRoomId(xRoomId)
                    .token(newSendInfo.getToken())
                    .recvSeq(i)
                    .recvAmt(BigDecimal.valueOf(recvAmtTmp))
                    .recvYn(RecvYnStatus.NOT_RECEIVED) // 뿌리기 시 받기 여부 디폴트 N
                    .createdAt(nowTime)
                    .build();

            recvInfoRepository.save(recvInfo);
        }

        return response(newSendInfo);
    }

    @Transactional(readOnly = true)
    public SendInfoResponse read(String xUserId, String token) {

        // 요청받은 토큰으로 7일 이내 Http Header X-USER-ID의 뿌리기 건 조회
        Optional<SendInfo> sendInfo = sendInfoRepository.findByxUserIdAndTokenAndCreatedAtAfter(xUserId, token, LocalDateTime.now().minusDays(7));

        log.debug("sendInfo : " + sendInfo);

        return sendInfo.map(sendData -> {
            List<RecvInfo> recvInfoList = recvInfoRepository.findByxUserIdAndTokenAndRecvYn(xUserId, token, RecvYnStatus.RECEIVED);
            SendInfoResponse sendInfoResponse = new SendInfoResponse();
            List<RecvUserInfoVO> recvUserInfoVOList = new ArrayList<>();

            // 받기 건이 있으면 List add 처리
            recvInfoList.forEach(recvInfo -> {
                RecvUserInfoVO recvUserInfoVO = new RecvUserInfoVO();
                recvUserInfoVO.setRecvAmt(recvInfo.getRecvAmt());
                recvUserInfoVO.setRecvUserId(recvInfo.getRecvUserId());
                recvUserInfoVOList.add(recvUserInfoVO);
            });

            sendInfoResponse.setCreatedAt(sendData.getCreatedAt()); // 뿌린 시각
            sendInfoResponse.setSendTotAmt(sendData.getSendTotAmt()); // 뿌린 금액
            sendInfoResponse.setRecvAmt(recvInfoList.stream().map(recvInfo -> recvInfo.getRecvAmt()).reduce(BigDecimal.ZERO, BigDecimal::add)); // 받기 완료된 금액
            sendInfoResponse.setRecvUserInfoVO(recvUserInfoVOList); // [받은 금액, 받은 사용자 아이디] 리스트
            return sendInfoResponse;
        })
                .orElseGet(() -> {
                    throw SendException.retrieveSendInfoFailed();
                });
    }

    @Transactional(readOnly = true)
    public String generateUniqueCode() {
        int tryCount = KeyConstant.TRY_COUNT_IN_COLLISION;
        String token = null;
        while (tryCount > 0) {
            token = tokenGenerator.generateToken();
            if (!sendInfoRepository.existsByToken(token)) {
                break;
            }
            tryCount--;
            if (tryCount == 0) {
                throw NotFoundException.createTokenFailed();
            }
        }
        return token;
    }

    private SendApiResponse response(SendInfo sendInfo) {

        // user object -> userApiResponse
        SendApiResponse sendApiResponse = SendApiResponse.builder()
                .token(sendInfo.getToken())
                .build();

        // Header + Data
        return sendApiResponse;
    }
}
