package com.chdlsp.pokeball.service;

import com.chdlsp.pokeball.PokeballApplicationTests;
import com.chdlsp.pokeball.model.token.TokenGenerator;
import com.chdlsp.pokeball.repository.RecvInfoRepository;
import com.chdlsp.pokeball.repository.SendInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SendApiServiceTest extends PokeballApplicationTests {

    @Autowired
    TokenGenerator tokenGenerator;

    @Test // token 은 3자리 문자열로 구성되며 예측이 불가능해야 합니다
    void generateUniqueCode() {
        String token = tokenGenerator.generateToken();
        assertThat(3, is(token.length()));
    }
}