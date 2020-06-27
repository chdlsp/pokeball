package com.chdlsp.pokeball.repository;

import com.chdlsp.pokeball.PokeballApplicationTests;
import com.chdlsp.pokeball.model.entity.RecvInfo;
import com.chdlsp.pokeball.model.entity.SendInfo;
import com.chdlsp.pokeball.model.enumClass.RecvYnStatus;
import com.chdlsp.pokeball.model.network.response.RecvApiResponse;
import com.chdlsp.pokeball.service.RecvApiService;
import com.chdlsp.pokeball.service.SendApiService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RecvInfoRepositoryTest extends PokeballApplicationTests {

    @Autowired
    private RecvInfoRepository recvInfoRepository;

    @Autowired
    private RecvApiService recvApiService;

    @Test
    public void create() {
        LocalDateTime nowTime = LocalDateTime.now();
        RecvInfo recvInfo = RecvInfo.builder()
                .xUserId("Test001")
                .xRoomId("Room001")
                .token("AbX")
                .recvSeq(1)
                .recvAmt(BigDecimal.valueOf(1000))
                .recvUserId("Test002")
                .updatedAt(nowTime)
                .recvYn(RecvYnStatus.NOT_RECEIVED)
                .build();

        RecvInfo newRecvInfo = recvInfoRepository.save(recvInfo);
        Assert.assertNotNull(newRecvInfo);
    }

    @Test
    public void update() {

        String xUserId = "Test001";
        String xRoomId = "Room001";
        String token = "AbX";

        LocalDateTime nowTime = LocalDateTime.now();
        RecvInfo recvInfo = RecvInfo.builder()
                .xUserId(xUserId)
                .xRoomId(xRoomId)
                .token(token)
                .recvSeq(1)
                .recvAmt(BigDecimal.valueOf(1000))
                .recvUserId("Test002")
                .updatedAt(nowTime)
                .recvYn(RecvYnStatus.NOT_RECEIVED)
                .build();

        RecvInfo newRecvInfo = recvInfoRepository.save(recvInfo);
        Assert.assertNotNull(newRecvInfo);

        xUserId = "Test002";
        RecvApiResponse updateRecvApiResponse = recvApiService.update(xUserId, xRoomId, token);
    }
}