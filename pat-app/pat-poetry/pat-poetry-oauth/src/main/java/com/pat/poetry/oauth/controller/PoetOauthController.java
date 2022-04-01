package com.pat.poetry.oauth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * LoginController
 *
 * @author chouway
 * @date 2022.03.31
 */
@Slf4j
@Controller
public class PoetOauthController {

    /**
     * 认证页面
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView require() {
        log.info("---认证页面---");
        return new ModelAndView("/ftl/login");
    }



    @GetMapping("/test/need_admin")
    @ResponseBody
    public  String admin() {
        return "need_admi2";
    }


    @RequestMapping("/api/test_0")
    @ResponseBody
    public String apiTest0(){
        return "api test_0";
    }
}
