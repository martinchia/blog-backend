package com.cloud.blog.error;

public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    public BusinessException(CommonError commonError, String errorMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrorMsg(errorMsg);
    }
    @Override
    public String getErrorMsg() {
        return commonError.getErrorMsg();
    }

    @Override
    public int getErrorCode() {
        return commonError.getErrorCode();
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.commonError.setErrorMsg(errorMsg);
        return this;
    }
}
