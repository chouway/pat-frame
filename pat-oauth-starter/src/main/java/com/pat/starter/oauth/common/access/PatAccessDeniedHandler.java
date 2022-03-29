package com.pat.starter.oauth.common.access;

import com.pat.common.base.bo.ResponseBO;
import com.pat.common.util.PatUtils;
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

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseBO resBO = new ResponseBO(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage(), null);
        PatUtils.writerError(resBO,response);
    }
}
