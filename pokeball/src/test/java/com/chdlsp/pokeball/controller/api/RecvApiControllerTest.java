package com.chdlsp.pokeball.controller.api;

import com.chdlsp.pokeball.PokeballApplicationTests;
import com.chdlsp.pokeball.model.network.request.SendApiRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecvApiControllerTest extends PokeballApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    void updateWithoutToken() throws Exception {

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/send";
        URI uri = new URI(baseUrl);

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("X-USER-ID", "Test001");
        requestHeader.add("X-ROOM-ID", "Room001");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
        HttpEntity<SendApiRequest> request = new HttpEntity<>(requestHeader);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                String.class);

        assertThat(400, is(response.getStatusCodeValue()));
        assertThat(false, is(Objects.requireNonNull(response.getBody()).contains("recv_amt"))); // 받은 금액
    }
}