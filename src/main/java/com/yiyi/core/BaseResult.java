package com.yiyi.core;

import java.io.Serializable;

/**
 * @author sdyang
 * @create 2018-01-16 11:02
 **/
public class BaseResult implements Serializable{

    private Boolean success;

    private String code;

    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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
