package com.chdlsp.pokeball.controller.api;

import com.chdlsp.pokeball.PokeballApplicationTests;
import com.chdlsp.pokeball.model.entity.SendInfo;
import com.chdlsp.pokeball.model.network.request.SendApiRequest;
import com.chdlsp.pokeball.repository.SendInfoRepository;
import com.chdlsp.pokeball.service.SendApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class SendApiControllerTest extends PokeballApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private SendApiService sendApiService;

    @Autowired
    private SendInfoRepository sendInfoRepository;

    @Test
    void createWithoutHeaderXUserId() throws Exception {

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/send";
        URI uri = new URI(baseUrl);

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("X-ROOM-ID", "Room001");

        SendApiRequest sendApiRequest = SendApiRequest.builder()
                .SendTotAmt(BigDecimal.valueOf(10000))
                .SendTotCnt(3)
                .build();

        HttpEntity<SendApiRequest> request = new HttpEntity<>(sendApiRequest, requestHeader);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertThat(400, is(result.getStatusCodeValue()));
        assertThat(false, is(Objects.requireNonNull(result.getBody()).contains("token"))); // 토큰 포함 여부 확인
    }

    @Test
    void createWithoutHeaderXRoomId() throws Exception {

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/send";
        URI uri = new URI(baseUrl);

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("X-USER-ID", "Test001");

        SendApiRequest sendApiRequest = SendApiRequest.builder()
                .SendTotAmt(BigDecimal.valueOf(10000))
                .SendTotCnt(3)
                .build();

        HttpEntity<SendApiRequest> request = new HttpEntity<>(sendApiRequest, requestHeader);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertThat(400, is(result.getStatusCodeValue()));
        assertThat(false, is(Objects.requireNonNull(result.getBody()).contains("token"))); // 토큰 포함 여부 확인
    }

    @Test
    void createWithoutRequestValue() throws Exception {

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/send";
        URI uri = new URI(baseUrl);

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("X-USER-ID", "Test001");
        requestHeader.add("X-ROOM-ID", "Room001");

        SendApiRequest sendApiRequest = SendApiRequest.builder()
                .build();

        HttpEntity<SendApiRequest> request = new HttpEntity<>(sendApiRequest, requestHeader);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertThat(500, is(result.getStatusCodeValue()));
        assertThat(false, is(Objects.requireNonNull(result.getBody()).contains("token"))); // 토큰 포함 여부 확인
    }

    @Test
    void createSuccessTest() throws Exception {

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/send";
        URI uri = new URI(baseUrl);

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("X-USER-ID", "Test001");
        requestHeader.add("X-ROOM-ID", "Room001");

        SendApiRequest sendApiRequest = SendApiRequest.builder()
                .SendTotAmt(BigDecimal.valueOf(10000))
                .SendTotCnt(3)
                .build();

        HttpEntity<SendApiRequest> request = new HttpEntity<>(sendApiRequest, requestHeader);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertThat(200, is(result.getStatusCodeValue()));
        assertThat(true, is(Objects.requireNonNull(result.getBody()).contains("token"))); // 토큰 포함 여부 확인

    }

    @Test
    void read() {

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

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/send";

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("X-USER-ID", "Test001");
        requestHeader.add("X-ROOM-ID", "Room001");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(newSendInfo.getToken());

        HttpEntity<SendApiRequest> request = new HttpEntity<>(requestHeader);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                String.class);

        log.info("builder.toUriString()" + builder.toUriString());
        log.info("response : " + response);

        assertThat(200, is(response.getStatusCodeValue()));
        assertThat(true, is(Objects.requireNonNull(response.getBody()).contains("created_at"))); // 뿌린 시각
        assertThat(true, is(Objects.requireNonNull(response.getBody()).contains("recv_amt"))); // 뿌린 금액
        assertThat(true, is(Objects.requireNonNull(response.getBody()).contains("send_tot_amt"))); // 받기 완료된 금액
        assertThat(true, is(Objects.requireNonNull(response.getBody()).contains("recv_user_info_vo"))); // 받기 완료된 정보

    }
}