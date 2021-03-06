package com.pat.poetry.oauth.controller;

import cn.hutool.core.io.IoUtil;
import com.pat.api.bo.CodeEnum;
import com.pat.api.bo.ResultBO;
import com.pat.api.entity.PatUser;
import com.pat.api.exception.BusinessException;
import com.pat.starter.oauth.common.util.PatCaptchaUtil;
import com.pat.starter.oauth.common.util.captcha.PatCaptcha;
import com.pat.starter.oauth.common.service.PatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private PatUserService patUserService;

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
            patUserService.resetPwdLocked(auth.getName());
        }

        if(StringUtils.hasText(url)){
            return "redirect:" + url;
        }
        return "redirect:/login";
    }




    /**
     * 注册页
     *
     * @return ModelAndView
     */
    @GetMapping("/signUp")
    public String signUp() {
        return "/ftl/signUp";
    }
    /**
     * 注册
     * @param patUser
     * @return
     */
    @RequestMapping(value = "/signUp.json")
    @ResponseBody
    public ResultBO<PatUser> signUp(@RequestBody PatUser patUser,String code,HttpServletRequest request) {
        ResultBO<PatUser> resultBO = new ResultBO<PatUser>();
        try {
            PatCaptchaUtil.checkCode(request,code);
            resultBO.setInfo(patUserService.signUp(patUser));
            resultBO.setSuccess(true);
            resultBO.setCode(CodeEnum.SUCCESS.getCode());
            resultBO.setMessage(CodeEnum.SUCCESS.getMessage());
        } catch(BusinessException e){
            log.error("busi error", e);
            resultBO.setCode(CodeEnum.BUSI_ERROR.getCode());
            resultBO.setMessage(e.getMessage());
        } catch (Exception e) {
            log.error("失败", e);
            resultBO.setCode(CodeEnum.ERROR.getCode());
            resultBO.setMessage(CodeEnum.ERROR.getMessage());
        }
        log.debug("signUp-->resultBO:code="+ resultBO.getCode());
        return resultBO;
    }


    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            PatCaptcha patCaptcha = PatCaptchaUtil.createPatCaptcha(200, 50, 4, 20);
            String code = patCaptcha.getCode();
//            log.info("captcha-->code={}", code);
            PatCaptchaUtil.sessionCode(request,code);
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


    @Autowired
    private TokenStore tokenStore;

    @RequestMapping(method = RequestMethod.DELETE, value = "/logoutByToken")
    @ResponseBody
    public void logoutByToken(String token) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (accessToken != null) {
            log.info("logoutByToken-->accessToken={}", accessToken);
            // 移除access_token
            tokenStore.removeAccessToken(accessToken);

            // 移除refresh_token
            if (accessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }
        }
    }


}
