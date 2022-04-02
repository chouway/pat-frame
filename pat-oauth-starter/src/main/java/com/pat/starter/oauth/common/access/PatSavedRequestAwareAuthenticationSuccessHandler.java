package com.pat.starter.oauth.common.access;

import com.pat.starter.oauth.common.util.HttpRespUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PatSavedRequestAwareAuthenticationSuccessHandler
 *
 * @author chouway
 * @date 2022.04.02
 */
public class PatSavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            User user = (User) authentication.getPrincipal();
            HttpRespUtil.writerOK(user,response);
        }else{
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
