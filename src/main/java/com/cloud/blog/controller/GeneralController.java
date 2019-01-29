package com.cloud.blog.controller;

import com.cloud.blog.error.BusinessException;
import com.cloud.blog.error.EmBusinessError;
import com.cloud.blog.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class GeneralController {
    public static final String CONTENT_TYPE_FORMED = "application/json";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        if(ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            response.put("errorCode", businessException.getErrorCode());
            response.put("errorMsg", businessException.getErrorMsg());
        }
        else {
            response.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            response.put("errorMsg", EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
        }
        return CommonReturnType.create(response, "fail");
    }
}
