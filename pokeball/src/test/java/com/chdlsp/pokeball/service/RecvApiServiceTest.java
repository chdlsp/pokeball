package com.chdlsp.pokeball.service;

import com.chdlsp.pokeball.PokeballApplicationTests;
import com.chdlsp.pokeball.model.entity.RecvInfo;
import com.chdlsp.pokeball.model.entity.SendInfo;
import com.chdlsp.pokeball.model.enumClass.RecvYnStatus;
import com.chdlsp.pokeball.repository.RecvInfoRepository;
import com.chdlsp.pokeball.repository.SendInfoRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

class RecvApiServiceTest extends PokeballApplicationTests {

    @Autowired
    private SendApiService sendApiService;

    @Autowired
    private RecvApiService recvApiService;

    @Autowired
    private SendInfoRepository sendInfoRepository;

    @Autowired
    private RecvInfoRepository recvInfoRepository;

    private String generatedToken = null;

    LocalDateTime nowTime = LocalDateTime.now();

    @BeforeEach
    void createdRecvInfo() {

        generatedToken = sendApiService.generateUniqueCode();

        String xUserId = "Test001";
        String xRoomId = "Room001";
        String token = generatedToken;

        SendInfo sendInfo = SendInfo.builder()
                .xUserId(xUserId)
                .xRoomId(xRoomId)
                .token(token)
                .SendTotAmt(BigDecimal.valueOf(10000))
                .SendTotCnt(3)
                .createdAt(nowTime)
                .build();

        SendInfo newSendInfo = sendInfoRepository.save(sendInfo);
        Assert.assertNotNull(newSendInfo);

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
    }

    @Test // 기 수령 여부 확인
    void isAlreadyReceived() {

        String recvXUserId = "Test002";
        String recvXRoomId = "Room001";
        String token = generatedToken;

        RecvInfo recvInfo = RecvInfo.builder()
                .xUserId("Test001") // 뿌린 사람 ID
                .xRoomId(recvXRoomId)
                .token(token)
                .recvSeq(0)
                .recvUserId(recvXUserId) // 받은 사람 ID : Test002
                .recvAmt(BigDecimal.valueOf(10000))
                .recvYn(RecvYnStatus.RECEIVED)
                .createdAt(nowTime)
                .build();

        recvInfoRepository.save(recvInfo);

        boolean isAlreadyReceived = recvApiService.isAlreadyReceived(recvXUserId, recvXRoomId, token);
        assertThat(isAlreadyReceived, is(true));

        recvXUserId = "Test003";
        boolean isReceivedYet = recvApiService.isAlreadyReceived(recvXUserId, recvXRoomId, token);
        assertThat(isReceivedYet, is(false));
    }

    @Test
    void isSendFromItself() {

        String xUserId = "Test001";
        String xRoomId = "Room001";
        String token = generatedToken;

        boolean isSendFromItselfTrue = recvApiService.isSendFromItself(xUserId, xRoomId, token);
        assertThat(isSendFromItselfTrue, is(true)); // Http Header 의 X-USER-ID 와 토큰의 뿌린 사람이 동일하면 true

        xUserId = "Test002"; // 다른 사람으로 테스트
        boolean isSendFromItselfFalse = recvApiService.isSendFromItself(xUserId, xRoomId, token);
        assertThat(isSendFromItselfFalse, is(false)); // Http Header 의 X-USER-ID 와 토큰의 뿌린 사람이 동일하면 false
    }

    @Test
    void isValidSendInfoExists() {

        String xUserId = "Test001";
        String xRoomId = "Room001";
        String token = sendApiService.generateUniqueCode();

        SendInfo sendInfo = SendInfo.builder()
                .xUserId(xUserId)
                .xRoomId(xRoomId)
                .token(token)
                .SendTotAmt(BigDecimal.valueOf(10000))
                .SendTotCnt(3)
                .createdAt(nowTime)
                .build();

        SendInfo newSendInfo = sendInfoRepository.save(sendInfo);
        Assert.assertNotNull(newSendInfo);

        boolean isValidSendInfoExists = recvApiService.isValidSendInfoExists(token);
        assertThat(isValidSendInfoExists, is(true)); // 요청받은 토큰으로 10분 이내 뿌리기 건 존재하면 true

        token = sendApiService.generateUniqueCode(); // false 테스트용 신규 토큰 발급

        SendInfo sendInfo10MinAgo = SendInfo.builder()
                .xUserId(xUserId)
                .xRoomId(xRoomId)
                .token(token)
                .SendTotAmt(BigDecimal.valueOf(10000))
                .SendTotCnt(3)
                .createdAt(nowTime.minusMinutes(11))
                .build();

        SendInfo newSendInfo10MinAgo = sendInfoRepository.save(sendInfo10MinAgo);
        Assert.assertNotNull(newSendInfo10MinAgo);

        boolean isValidSendInfo = recvApiService.isValidSendInfoExists(token);
        assertThat(isValidSendInfo, is(false)); // 요청받은 토큰으로 10분 이내 뿌리기 건 없으면 false
    }
}