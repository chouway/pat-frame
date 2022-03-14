package com.pat.api.bo;

import java.io.Serializable;

/**
 * 返回服务器状态的bean类
 */
public class ResponseBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer status;
    private String msg;
    private Object obj;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public ResponseBO(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }
}
