package com.pat.starter.oauth.common.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pat.api.bo.ResponseBO;
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
        writerError(resBO,response);
    }

    public void writerError(ResponseBO responseBO, HttpServletResponse response) throws IOException {
        response.setStatus(responseBO.getStatus());
        response.setContentType("application/json,charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), responseBO);
    }
}
