package com.pat.api.service.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.pat.api.bo.CodeEnum;
import com.pat.api.bo.ResultBO;
import com.pat.api.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClientController
 *
 * @author chouway
 * @date 2022.04.18
 */
@Slf4j
@Controller
@RequestMapping(value = "/client")
public class ClientController {

    @Value("${app.oauth.client.oauth_url}")
    private String OAUTH_URL;

    @Value("${app.oauth.client.oauth_token}")
    private String OAUTH_TOKEN;

    @Value("${app.oauth.client.oauth_refresh_token}")
    private String OAUTH_REFRESH_TOKEN;

    @RequestMapping(value = "/token")
    @ResponseBody
    public ResultBO token(String code) {
        ResultBO<Object> resultBO = new ResultBO<Object>();
        try {
            log.info("token-->code={}", code);
            String body = String.format(OAUTH_TOKEN, code);
//          log.info("token-->url={},body={}", OAUTH_URL,body);
            String result = HttpUtil.post(OAUTH_URL, body);
            log.info("token-->result={}", result);

            resultBO.setInfo(JSON.parse(result));
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
        log.debug("token-->resultBO:code="+ resultBO.getCode());
        return resultBO;
    }

    @RequestMapping(value = "/refreshToken")
    @ResponseBody
    public ResultBO refreshToken(String refreshToken) {
        ResultBO<Object> resultBO = new ResultBO<Object>();
        try {
            log.info("refreshToken-->refreshToken={}", refreshToken);
            String result = HttpUtil.post(OAUTH_URL, String.format(refreshToken, refreshToken));
            resultBO.setInfo(JSON.parse(result));
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
        log.debug("refreshToken-->resultBO:code="+ resultBO.getCode());
        return resultBO;
    }
}
