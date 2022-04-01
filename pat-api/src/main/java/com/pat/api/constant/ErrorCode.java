package com.pat.api.constant;

import com.pat.api.bo.ResultBO;

/**
 * ErrorCode
 * @author zhouyw
 * @date 2019.09.18
 */
public enum ErrorCode {

    SERVICE_FAILE("E00000","服务处理失败"),
    OAUTH_ACCESS_DENIED("E10000","权限不足"),
    OAUTH_BAD_CREDENTIALS("E10001","凭证无效"),
    OAUTH_TOKEN_NOT_FOUND("E10002","请上送TOKEN");


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

    public static ResultBO errorBO(ErrorCode errorCode){
        ResultBO resultBO = new ResultBO();
        resultBO.setCode(errorCode.getCode());
        resultBO.setMessage(errorCode.getName());
        resultBO.setSuccess(false);
        return resultBO;
    }
}
