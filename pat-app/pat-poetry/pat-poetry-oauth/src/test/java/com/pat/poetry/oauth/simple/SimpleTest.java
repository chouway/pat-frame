package com.pat.poetry.oauth.simple;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import com.pat.starter.oauth.common.util.PatCaptchaUtil;
import com.pat.starter.oauth.common.util.captcha.PatCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * SimpleTest
 *
 * @author chouway
 * @date 2022.04.02
 */
@Slf4j
public class SimpleTest {
    /**
     * https://hutool.cn/docs/#/captcha/%E6%A6%82%E8%BF%B0?id=%e8%87%aa%e5%ae%9a%e4%b9%89%e9%aa%8c%e8%af%81%e7%a0%81
     * 自定义验证码
     */
    @Test
    public void captcha(){
        GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(300, 50,4);
        gifCaptcha.write("D:\\tmp\\captcha.gif");
        //输出code
        log.info("code={}",gifCaptcha.getCode());


        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(300, 50,4,50);
        shearCaptcha.write("D:\\tmp\\shear.png");
        //输出code
        log.info("code={}",shearCaptcha.getCode());


        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 60,4,50);
        lineCaptcha.write("D:\\tmp\\line.png");
        //输出code
        log.info("code={}",lineCaptcha.getCode());


    }

    @Test
    public void patCaptcha(){
        PatCaptcha patCaptcha = PatCaptchaUtil.createPatCaptcha(200, 60, 4, 30);
        patCaptcha.write("D:\\tmp\\patCaptcha.png");
        //输出code
        log.info("patCaptcha.code={}",patCaptcha.getCode());

    }
}
