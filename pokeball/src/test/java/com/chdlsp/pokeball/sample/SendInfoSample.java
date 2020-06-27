package com.chdlsp.pokeball.sample;

import com.chdlsp.pokeball.PokeballApplicationTests;
import com.chdlsp.pokeball.model.entity.SendInfo;
import com.chdlsp.pokeball.repository.SendInfoRepository;
import com.chdlsp.pokeball.service.SendApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
public class SendInfoSample extends PokeballApplicationTests {

    private Random random = new Random();

    @Autowired
    private SendApiService sendApiService;

    @Autowired
    private SendInfoRepository sendInfoRepository;

    @Test
    public void createAll() {
        createSendInfo();
    }

    @Test
    public void createSendInfo() {

        LocalDateTime nowTime = LocalDateTime.now();

        for(int i=0; i<11; i++) {
            SendInfo sendInfo = SendInfo.builder()
                    .xUserId("Id" + sendApiService.generateUniqueCode())
                    .xRoomId(sendApiService.generateUniqueCode() + "Room")
                    .token(sendApiService.generateUniqueCode())
                    .SendTotAmt(BigDecimal.valueOf(random.nextInt(20000)))
                    .SendTotCnt(random.nextInt(10))
                    .createdAt(nowTime)
                    .build();
            SendInfo newSendInfo = sendInfoRepository.save(sendInfo);
            Assert.assertNotNull(newSendInfo);
        }
    }
}