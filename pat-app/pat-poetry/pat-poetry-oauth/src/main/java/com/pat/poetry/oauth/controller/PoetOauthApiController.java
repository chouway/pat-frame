package com.pat.poetry.oauth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
