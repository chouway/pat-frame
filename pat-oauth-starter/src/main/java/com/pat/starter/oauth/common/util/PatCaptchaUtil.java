package com.pat.starter.oauth.common.util;

import cn.hutool.captcha.CaptchaUtil;
import com.pat.api.constant.PatConstant;
import com.pat.starter.oauth.common.util.captcha.PatCaptcha;
import org.springframework.security.authentication.AuthenticationServiceException;

import javax.servlet.http.HttpServletRequest;

/**
 * PatCaptchaUtil
 *
 * @author chouway
 * @date 2022.04.02
 */
public class PatCaptchaUtil extends CaptchaUtil {


    public static PatCaptcha createPatCaptcha(int width, int height, int codeCount, int lineCount){
        return new PatCaptcha(width,height,codeCount,lineCount);
    };

    /**
     * @param code  用户手办输的验证码
     * @param verify_code session的验证码
     */
    public static void checkCode(HttpServletRequest request, String code) {
        String verify_code = (String) request.getSession().getAttribute(PatConstant.VERIFY_CODE);
        if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
            //验证码不正确
            throw new AuthenticationServiceException("验证码不正确");
        }
    }

    /**
     * 往session 放置code
     * @param request
     * @param code
     */
    public static void sessionCode(HttpServletRequest request, String code) {
        request.getSession().setAttribute(PatConstant.VERIFY_CODE, code);
    }


}
