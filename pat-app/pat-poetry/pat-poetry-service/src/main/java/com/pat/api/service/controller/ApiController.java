package com.pat.api.service.controller;

import com.pat.api.bo.CodeEnum;
import com.pat.api.bo.EsSearchBO;
import com.pat.api.bo.PoetSearchResultBO;
import com.pat.api.bo.ResultBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * ApiController
 *
 * @author chouway
 * @date 2022.04.18
 */
@Slf4j
@Controller
@RequestMapping(value = "/poet-api")
public class ApiController {

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        log.info("test-->principal={}", principal);
        return "test-ok";
    }

}
