package com.pat.api.exception;

import com.pat.api.constant.ErrorCode;

/**
 * BusinessException
 * 业务异常
 * @author zhouyw
 * @date 2019.09.03
 */
public class BusinessException extends RuntimeException {

    private String code;

    private ErrorCode errorCode = ErrorCode.SERVICE_FAILE;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public BusinessException() {

    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode!=null?errorCode.getName():null);
        this.code = errorCode!=null?errorCode.getCode():null;
        this.errorCode = errorCode;
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
