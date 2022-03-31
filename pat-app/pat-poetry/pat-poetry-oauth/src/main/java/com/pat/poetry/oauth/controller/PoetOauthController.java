package com.pat.poetry.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * LoginController
 *
 * @author chouway
 * @date 2022.03.31
 */
@Controller
public class PoetOauthController {

    @RequestMapping("/login")
    @ResponseBody
    public String login(){
        return "login page";
    }





    @RequestMapping("/api/test_0")
    @ResponseBody
    public String apiTest0(){
        return "api test_0";
    }
}
