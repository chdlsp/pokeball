package com.chdlsp.pokeball.controller.constant;

public class ErrorConstant {

    public static final String ACCESS_DENIED = "Access Denied";
    public static final String CREATE_TOKEN_FAILED = "Failed To Create New Token";

    public static final String WRONG_HEADER_CONTENTS = "Http Header User/Room Value Is Wrong";

    public static final String TOKEN_VALUE_NOTFOUND = "Cannot Find out Token Value";
    public static final String USER_VALUE_NOTFOUND = "Cannot Find out User Value";
    public static final String ROOM_VALUE_NOTFOUND = "Cannot Find out Room Value";

    // Send Error
    public static final String NECESSARY_VALUE_NULL = "Necessary Value For Send Api Is Missing";
    public static final String WRONG_SEND_AMOUNT = "The Minimum Amount Of Send Money Should Be 1,000 WON";

    // Recv Error
    public static final String RETRIEVE_RECV_INFO_FAILED = "Can't Find Proper Recv Info";
    public static final String ALREADY_RECEIVED = "You Already Received Money";
    public static final String SEND_FROM_ITSELF = "This Request Is Send From Yourself";
    public static final String NOT_FOUND_PROPER_RECV_INFO = "Can't Find Proper Recv Info";

    // Retrieve Error
    public static final String RETRIEVE_SEND_INFO_FAILED = "Can't Find Proper Send Info";
}
