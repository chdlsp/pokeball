package com.chdlsp.pokeball.exception;

import com.chdlsp.pokeball.controller.constant.ErrorConstant;
import org.springframework.http.HttpStatus;

public class RecvException extends BaseException {
    private RecvException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }

    public static RecvException necessaryValueNullError(){return new RecvException(ErrorConstant.NECESSARY_VALUE_NULL); }
    public static RecvException alreadyReceivedException(){return new RecvException(ErrorConstant.ALREADY_RECEIVED); }
    public static RecvException sendFromItself(){return new RecvException(ErrorConstant.SEND_FROM_ITSELF); }
    public static RecvException retrieveRecvInfoFailed(){return new RecvException(ErrorConstant.RETRIEVE_RECV_INFO_FAILED); }
    public static RecvException findProperRecvInfoError(){return new RecvException(ErrorConstant.NOT_FOUND_PROPER_RECV_INFO); }





}
