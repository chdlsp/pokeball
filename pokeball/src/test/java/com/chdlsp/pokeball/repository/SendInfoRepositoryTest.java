package com.chdlsp.pokeball.repository;

import com.chdlsp.pokeball.PokeballApplicationTests;
import com.chdlsp.pokeball.model.entity.SendInfo;

import com.chdlsp.pokeball.service.SendApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Slf4j
public class SendInfoRepositoryTest extends PokeballApplicationTests {

    @Autowired
    private SendInfoRepository sendInfoRepository;

    @Autowired
    private SendApiService sendApiService;

    @Test
    public void create() {
        LocalDateTime nowTime = LocalDateTime.now();
        SendInfo sendInfo = SendInfo.builder()
                .xUserId("Test001")
                .xRoomId("Room001")
                .token(sendApiService.generateUniqueCode())
                .SendTotAmt(BigDecimal.valueOf(100000))
                .SendTotCnt(1)
                .createdAt(nowTime)
                .build();

        SendInfo newSendInfo = sendInfoRepository.save(sendInfo);
        Assert.assertNotNull(newSendInfo);
    }
}