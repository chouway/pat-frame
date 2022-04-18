package com.pat.poetry.oauth.controller;

import cn.hutool.core.io.IoUtil;
import com.pat.api.bo.CodeEnum;
import com.pat.api.bo.ResultBO;
import com.pat.api.entity.PatUser;
import com.pat.api.exception.BusinessException;
import com.pat.starter.oauth.common.util.PatCaptchaUtil;
import com.pat.starter.oauth.common.util.captcha.PatCaptcha;
import com.pat.starter.oauth.server.service.PatUserService;
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
@RequestMapping("/oauth-api")
public class PoetOauthApiController {



    @RequestMapping("/test_0")
    @ResponseBody
    public String apiTest0() {
        return "api test_0";
    }

    @RequestMapping("/test_1")
    @ResponseBody
    public String apiTest1() {
        return "api test_1";
    }
}
