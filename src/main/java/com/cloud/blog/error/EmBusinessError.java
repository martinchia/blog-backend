package com.cloud.blog.error;

public enum EmBusinessError implements CommonError  {
    // General error type, start with digit 1
    PARAMETER_VALIDATION_ERROR(10001, "Parameters are illegal."),
    UNKNOWN_ERROR(10002, "Unknown error."),
    // error code start with digit 2 means error related with user
    USER_NOT_EXIST(20001, "User does not exist.")
    ;

    private int errorCode;
    private String errorMsg;

    private EmBusinessError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
