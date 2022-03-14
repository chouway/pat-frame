package com.pat.api.bo;

/**
 * @Description 状态码
 * @Author ywzhou
 * @Date 2019/07/26
 * @Param
 * @Return
 */
public enum CodeEnum {
    // 成功返回
    SUCCESS("ok", "成功"),
    // 查询失败
    QUERY_ERROR("E100", "查询失败"),
    //新建或修改失败
    MODIFY_ERROR("E201", "新建或修改失败"),
    //数据删除失败
    DELETE_ERROR("E204", "删除失败"),
    //请求错误
    ERROR("E400", "请求错误"),
    //没有授权
    NOT_AUTHORIZATION("E401", "没有授权"),
    //用户名密码错误
    LOGIN_ERROR_USER("E402", "用户名密码错误"),
    //锁定的用户
    LOCKED_USER("E403", "锁定的用户"),
    //登出失败
    LOGOUT_ERROR("E405", "登出失败"),
    // 无权访问
    ACCESS_DENIED("E406", "无权访问"),
    //服务器发生错误
    SERVER_ERROR("E500", "服务器发送错误");

    private String code;
    private String message;

    CodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
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
