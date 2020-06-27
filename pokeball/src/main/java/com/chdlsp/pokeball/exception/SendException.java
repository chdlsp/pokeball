package com.chdlsp.pokeball.exception;

import com.chdlsp.pokeball.controller.constant.ErrorConstant;
import org.springframework.http.HttpStatus;

public class SendException extends BaseException  {
    private SendException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }

    // Send Exception
    public static SendException wrongSendAmtError(){return new SendException(ErrorConstant.WRONG_SEND_AMOUNT); }
    public static SendException necessaryValueNullError(){return new SendException(ErrorConstant.NECESSARY_VALUE_NULL); }

    // Retrieve Exception
    public static SendException retrieveSendInfoFailed(){return new SendException(ErrorConstant.RETRIEVE_SEND_INFO_FAILED); }

}
