package com.pat.starter.oauth.common.access;

import com.pat.api.constant.ErrorCode;
import com.pat.starter.oauth.common.util.HttpRespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PatSimpleUrlAuthenticationFailureHandler
 *
 * @author chouway
 * @date 2022.04.02
 */
@Slf4j
public class PatSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{

    @PostConstruct
    public void init(){
        setDefaultFailureUrl("/login?error");
    }



    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            log.error(exception.getMessage());
            if (exception instanceof LockedException) {
                HttpRespUtil.writerError(ErrorCode.OAUTH_LOCKED, response);
            } else if (exception instanceof CredentialsExpiredException) {
                HttpRespUtil.writerError(ErrorCode.OAUTH_CREDENTIALS_EXPIRED, response);
            } else if (exception instanceof AccountExpiredException) {
                HttpRespUtil.writerError(ErrorCode.OAUTH_ACCOUNT_EXPIRED, response);
            } else if (exception instanceof DisabledException) {
                HttpRespUtil.writerError(ErrorCode.OAUTH_DISABLED, response);
            } else if (exception instanceof BadCredentialsException) {
                HttpRespUtil.writerError(ErrorCode.OAUTH_BAD_CREDENTIALS_H, response);
            } else {
                HttpRespUtil.writerError(ErrorCode.SERVICE_FAILE, response);
            }
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
