package com.pat.poetry.oauth.controller;

import cn.hutool.core.io.IoUtil;
import com.pat.api.constant.PatConstant;
import com.pat.starter.oauth.common.util.PatCaptchaUtil;
import com.pat.starter.oauth.common.util.captcha.PatCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * LoginController
 *
 * @author chouway
 * @date 2022.03.31
 */
@Slf4j
@Controller
public class PoetOauthController {

    @Value("${app.oauth.server.successUrl}")
    private  String successUrl;

    /**
     * 默认页
     * @param user
     * @return
     */
    @GetMapping("/")
    public String index(Principal user) {
        if(user!=null){
            return  "redirect:" + successUrl;
        }
        return "redirect:/login";
    }
    /**
     * 登录页
     *
     * @return ModelAndView
     */
    @GetMapping("/login")
    public String login(Principal user) {
        if(user!=null){
            return  "redirect:" + successUrl;
        }
        return "/ftl/login";
    }

    /**
     * 退出
     * @param request
     * @param response
     * @param url
     * @return
     */
    @RequestMapping({"/logout"})
    public String logout(HttpServletRequest request, HttpServletResponse response,String url) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        if(StringUtils.hasText(url)){
            return "redirect:" + url;
        }
        return "redirect:/login";


    }


    @GetMapping("/captcha")
    public void captcha(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            PatCaptcha patCaptcha = PatCaptchaUtil.createPatCaptcha(200, 57, 4, 20);
            String code = patCaptcha.getCode();
//            log.info("captcha-->code={}", code);
            session.setAttribute(PatConstant.VERIFY_CODE, code);
            outputStream = response.getOutputStream();
            patCaptcha.write(outputStream);
        } catch (Exception e) {
            log.error("error:captcha-->e={}", e, e);
            throw new RuntimeException("获取验证码失败");
        } finally {
            IoUtil.close(outputStream);
        }
    }

    @GetMapping("/test/loginSuccess")
    @ResponseBody
    public String admin() {
        return "login success";
    }


    @RequestMapping("/api/test_0")
    @ResponseBody
    public String apiTest0() {
        return "api test_0";
    }

    @RequestMapping("/api/test_1")
    @ResponseBody
    public String apiTest1() {
        return "api test_1";
    }
}
