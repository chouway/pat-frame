package com.pat.starter.oauth.common.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pat.api.bo.ResponseBO;
import com.pat.api.bo.ResultBO;
import com.pat.api.constant.ErrorCode;
import com.pat.starter.oauth.common.util.HttpRespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * createby zhouyw on 2020.07.01
 */
@Slf4j
public class PatAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * HttpServletResponse.SC_FORBIDDEN
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("handle-->accessDeniedException.msg={}", accessDeniedException.getMessage());
        HttpRespUtil.writerError(ErrorCode.OAUTH_ACCESS_DENIED,response);
    }
}
