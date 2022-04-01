package com.pat.starter.oauth.common.access;

import com.pat.api.constant.ErrorCode;
import com.pat.starter.oauth.common.util.HttpRespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PatAuthenticationEntryPoint
 *
 * @author chouway
 * @date 2022.04.01
 */
@Slf4j
public class PatAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error(e.getMessage());
        if(e instanceof BadCredentialsException){
            //如果是client_id和client_secret相关异常 返回自定义的数据格式
            HttpRespUtil.writerError(ErrorCode.OAUTH_ACCESS_DENIED,response);
        }else if(e instanceof InsufficientAuthenticationException){
            //如果是没有携带token
            HttpRespUtil.writerError(ErrorCode.OAUTH_TOKEN_NOT_FOUND,response);
        }else {
            super.commence(request,response,e);
        }

    }
}
