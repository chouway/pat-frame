package com.pat.api.constant;

import com.pat.api.bo.ResultBO;

/**
 * ErrorCode
 * @author zhouyw
 * @date 2019.09.18
 */
public enum ErrorCode {

    SERVICE_FAILE("E00000","服务错误，请稍候再试"),
    OAUTH_ACCESS_DENIED("E10000","权限不足"),
    OAUTH_BAD_CREDENTIALS("E10001","凭证无效"),
    OAUTH_TOKEN_NOT_FOUND("E10002","请上送TOKEN"),
    OAUTH_LOCKED("E10003","账户被锁定!"),
    OAUTH_CREDENTIALS_EXPIRED("E10004","密码过期"),
    OAUTH_ACCOUNT_EXPIRED("E10005","账户过期"),
    OAUTH_DISABLED("E10006","账户被禁用"),
    OAUTH_BAD_CREDENTIALS_H("E10007","用户名或者密码输入错误");


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
