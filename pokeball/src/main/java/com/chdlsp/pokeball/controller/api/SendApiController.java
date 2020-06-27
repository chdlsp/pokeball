package com.chdlsp.pokeball.controller.api;

import com.chdlsp.pokeball.exception.SendException;
import com.chdlsp.pokeball.model.network.request.SendApiRequest;
import com.chdlsp.pokeball.model.network.response.SendApiResponse;
import com.chdlsp.pokeball.model.network.response.SendInfoResponse;
import com.chdlsp.pokeball.service.SendApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/send")
public class SendApiController {

    @Autowired
    private SendApiService sendApiService;

    @PostMapping("")
    public SendApiResponse create(@RequestHeader(value = "X-USER-ID") String xUserId, @RequestHeader(value = "X-ROOM-ID") String xRoomId, @RequestBody SendApiRequest sendApiRequest) {

        // 받을 인원, 받을 금액 요청 입력값이 없는 경우 예외처리
        if(sendApiRequest.getSendTotCnt() == null || sendApiRequest.getSendTotAmt() == null) {
            throw SendException.necessaryValueNullError();
        }

        // 뿌리기 금액이 1000원 이하인 경우 예외 처리
        if(BigDecimal.valueOf(1000).compareTo(sendApiRequest.getSendTotAmt()) > 0) {
            throw SendException.wrongSendAmtError();
        }

        return sendApiService.create(xUserId, xRoomId, sendApiRequest);
    }

    @GetMapping("{token}")
    public SendInfoResponse read(@RequestHeader(value = "X-USER-ID") String xUserId, @PathVariable String token) {

        // 토큰 입력값이 존재하지 않는 경우 예외 처리
        if(token == null) {
            throw SendException.necessaryValueNullError();
        }
        return sendApiService.read(xUserId, token);
    }
}
