package com.chdlsp.pokeball.model.token;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TokenGenerator {
    private static final int TOKEN_LENGTH = 3;
    protected static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    char randomChar(){
        Random r = new Random();
        return ALPHABET.charAt(r.nextInt(ALPHABET.length()));
    }

    String randomUUID(int length){
        StringBuilder sb = new StringBuilder();
        while(length > 0){
            length--;
            sb.append(randomChar());
        }
        return sb.toString();
    }

    public String generateToken() {
        return randomUUID(TOKEN_LENGTH);
    }
}
