package com.pat.api.constant;

/**
 * ErrorCode
 * @author zhouyw
 * @date 2019.09.18
 */
public enum ErrorCode {

    SERVICE_FAILE("E00000","服务处理失败");

    private String code;

    private String name;

    private ErrorCode(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
