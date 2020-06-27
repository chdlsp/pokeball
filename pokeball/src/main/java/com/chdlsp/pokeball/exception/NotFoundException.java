package com.chdlsp.pokeball.exception;

import com.chdlsp.pokeball.controller.constant.ErrorConstant;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    private NotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }

    public static NotFoundException accessDenied(){
        return new NotFoundException(ErrorConstant.ACCESS_DENIED);
    }
    public static NotFoundException createTokenFailed(){return new NotFoundException(ErrorConstant.CREATE_TOKEN_FAILED); }

    public static NotFoundException wrongHeaderContents(){return new NotFoundException(ErrorConstant.WRONG_HEADER_CONTENTS); }

    public static NotFoundException notFoundTokenValue(){return new NotFoundException(ErrorConstant.TOKEN_VALUE_NOTFOUND); }
    public static NotFoundException notFoundUserValue(){return new NotFoundException(ErrorConstant.USER_VALUE_NOTFOUND); }
    public static NotFoundException notFoundRoomValue(){return new NotFoundException(ErrorConstant.ROOM_VALUE_NOTFOUND); }

}