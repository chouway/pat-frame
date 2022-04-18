package com.pat.starter.oauth.server.event;

import com.pat.starter.oauth.common.service.PatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

/**
 * PatAuthFailureListener
 *
 * @author chouway
 * @date 2022.04.04
 */
public class PatAuthFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private PatUserService patUserService;
    /**
     * 记录当天密码登录 失败次数
     * @param authenticationFailureBadCredentialsEvent
     */
    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
        String username = authenticationFailureBadCredentialsEvent.getAuthentication().getPrincipal().toString();
        patUserService.recodePwdFail(username);
    }
}
