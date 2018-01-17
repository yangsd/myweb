package com.yiyi.core;

import java.io.Serializable;

/**
 * @author sdyang
 * @create 2018-01-16 11:02
 **/
public class BaseResult implements Serializable{

    private Boolean isSuccess;

    private String code;

    private String message;

    public BaseResult() {
    }

    public BaseResult(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
