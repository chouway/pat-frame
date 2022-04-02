package com.pat.starter.oauth.server.filter;

import com.pat.api.constant.PatConstant;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * PatUsernamePwdAuthenticationFilter
 * https://cloud.tencent.com/developer/article/1914286
 * Spring Security---详解登录步骤
 *
 * @author chouway
 * @date 2022.04.02
 */
@Slf4j
public class PatUsernamePwdAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       try{
           if (!request.getMethod().equals("POST")) {
               throw new AuthenticationServiceException(
                       "Authentication method not supported: " + request.getMethod());
           }
           String verify_code = (String) request.getSession().getAttribute(PatConstant.VERIFY_CODE);
           if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
               Map<String, String> loginData = new HashMap<String, String>();
               try {
                   loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
               } catch (IOException e) {
               }finally {
                   String code = loginData.get("code");
                   checkCode(response, code, verify_code);
               }
               String username = loginData.get(getUsernameParameter());
               String password = loginData.get(getPasswordParameter());
               if (username == null) {
                   username = "";
               }
               if (password == null) {
                   password = "";
               }
               username = username.trim();
               UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
               setDetails(request, authRequest);
               return this.getAuthenticationManager().authenticate(authRequest);
           } else {
               checkCode(response, request.getParameter("code"), verify_code);
               return super.attemptAuthentication(request, response);
           }
       }catch(Exception e){
           log.error("error:attemptAuthentication-->e={}", e,e);
           throw e;
       }
    }

    public void checkCode(HttpServletResponse resp, String code, String verify_code) {
        if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
            //验证码不正确
            throw new AuthenticationServiceException("验证码不正确");
        }
    }
}
