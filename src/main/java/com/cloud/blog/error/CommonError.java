package com.cloud.blog.error;

public interface CommonError {
    public String getErrorMsg();
    public int getErrorCode();
    public CommonError setErrorMsg(String errorMsg);
}
