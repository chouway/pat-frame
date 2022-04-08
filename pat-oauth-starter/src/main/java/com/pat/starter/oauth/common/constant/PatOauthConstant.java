package com.pat.starter.oauth.common.constant;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * PatOauthConstant
 *
 * @author chouway
 * @date 2022.03.29
 */
public class PatOauthConstant {

    private PatOauthConstant(){

    }

    /**
     * 手机验证码
     */
    public final static String CACHE_SMS_CODE = "sms-code::";

    /**
     * 邮件验证码
     */
    public final static String CACHE_EMAIL_CODE = "email-code::";


    /**
     * 超管
     */
    public static final String ROLE_SUPER = "SUPER";

    /**
     * 普通用户
     */
    public static final String ROLE_USER = "USER";

    /**
     * 游客
     */
    public static final String ROLE_NEMO = "NEMO";


    /**
     * 待核验
     */
    public static final String STATUS_INIT = "0";

    /**
     * 已绑定（邮箱 或者 手机号）
     */
    public static final String STATUS_BIND = "1";

    /**
     * 已实名
     */
    public static final String STATUS_REAL = "2";

    /**
     * 用户名正则 判断  只能由 英文字母 、 数字 、  下划线 三者组成 至少6位
     */
    public static final String REGEX_USER_NAME = "[a-zA-Z0-9_]{6,}";
    /**
     * 角色池
     */
    public static final Map<String,String> ROLE_MAP;
    static {
        Map<String,String> TEM_ROLE_MAP = new HashMap<String, String>();
        TEM_ROLE_MAP.put(ROLE_SUPER,"超管");
        TEM_ROLE_MAP.put(ROLE_USER,"用户");
        TEM_ROLE_MAP.put(ROLE_NEMO,"游客");
        ROLE_MAP = Collections.unmodifiableMap(TEM_ROLE_MAP);
    }
}
