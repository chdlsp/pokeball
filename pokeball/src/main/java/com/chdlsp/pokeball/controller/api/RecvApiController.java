package com.chdlsp.pokeball.controller.api;

import com.chdlsp.pokeball.exception.RecvException;
import com.chdlsp.pokeball.exception.SendException;
import com.chdlsp.pokeball.model.network.response.RecvApiResponse;
import com.chdlsp.pokeball.model.network.response.SendInfoResponse;
import com.chdlsp.pokeball.service.RecvApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/recv")
public class RecvApiController {

    @Autowired
    private RecvApiService recvApiService;

    @PutMapping("{token}")
    public RecvApiResponse update(@RequestHeader(value = "X-USER-ID") String xUserId, @RequestHeader(value = "X-ROOM-ID") String xRoomId, @PathVariable String token) {

        // 토큰 입력값이 존재하지 않는 경우 예외 처리
        if(token == null) {
            throw RecvException.necessaryValueNullError();
        }

        // 요청받은 token 으로 10분 이내 발급된 뿌리기 건 존재여부 확인 (없는 경우 Exception)
        if(!recvApiService.isValidSendInfoExists(token)) {
            throw RecvException.retrieveRecvInfoFailed();
        }

        // 본인이 뿌리기를 처리한 건인지 확인
        if(recvApiService.isSendFromItself(xUserId, xRoomId, token)) {
            throw RecvException.sendFromItself();
        }

        // 기 수령 여부 확인
        if(recvApiService.isAlreadyReceived(xUserId, xRoomId, token)) {
            throw RecvException.alreadyReceivedException();
        }

        // 뿌리기 건 받기 처리
        return recvApiService.update(xUserId, xRoomId, token);
    }
}
