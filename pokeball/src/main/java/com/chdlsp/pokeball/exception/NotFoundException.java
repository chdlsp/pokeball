package com.chdlsp.pokeball.exception;

import com.chdlsp.pokeball.controller.constant.ErrorConstant;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    private NotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }

    public static NotFoundException getAccessDenied(){
        return new NotFoundException(ErrorConstant.ACCESS_DENIED);
    }

}