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
