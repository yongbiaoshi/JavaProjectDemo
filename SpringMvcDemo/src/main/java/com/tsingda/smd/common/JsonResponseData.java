package com.tsingda.smd.common;

import java.io.Serializable;

public class JsonResponseData implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 5445948775965945989L;
    private boolean success;
    private String msg;
    private Object data;

    public JsonResponseData() {
    }

    public JsonResponseData(boolean success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
