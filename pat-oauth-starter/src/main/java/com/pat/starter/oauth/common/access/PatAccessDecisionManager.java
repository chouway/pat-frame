package com.pat.starter.oauth.common.access;

import com.pat.starter.oauth.common.constant.PatOauthConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * CustomAccessDecisionManager
 *
 * 自定义权限认证，获取url判断是否有权限
 *
 * 待整合 dubbo服务
 *
 * @author zhouyw
 * @date 2020.06.30
 */
@Slf4j
public class PatAccessDecisionManager implements AccessDecisionManager {

    private List<AccessDecisionVoter<? extends Object>> decisionVoters;

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if(configAttributes.contains(PatOauthConstant.ROLE_NEMO)){
            throw new AccessDeniedException("请先登录授权");
        }
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
//        System.out.println("requestUrl>>" + requestUrl);
        log.info("-->method={},requestUri={}", method,requestUri);
       /* if(requestUri.indexOf("/test/need_admin")==0){
            return;
        }*/
        // 当前用户所具有的权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//      System.out.println("authorities=" + authorities);
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(PatOauthConstant.ROLE_SUPER)) {//当前是超管则直接 放行
                return;
            }
            if(configAttributes.contains(grantedAuthority.getAuthority())){//当前url 匹配的 role
                return;
            }
            //custom 自定义逻辑
        }
        throw new AccessDeniedException("无访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }


}