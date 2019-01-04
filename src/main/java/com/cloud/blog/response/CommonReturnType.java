package com.cloud.blog.response;

public class CommonReturnType {
    private String status; // 'sccuess' or 'fail'
    private Object data;

    // if status == 'success' -> return the needed data
    // if status == 'fail' -> return general error format

    public static CommonReturnType create(Object res) {
        return CommonReturnType.create(res, "success");
    }

    public static CommonReturnType create(Object res, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(res);
        type.setStatus(status);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
