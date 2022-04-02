package com.pat.starter.oauth.common.util;

import cn.hutool.captcha.CaptchaUtil;
import com.pat.starter.oauth.common.util.captcha.PatCaptcha;

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
}
