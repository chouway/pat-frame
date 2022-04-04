package com.pat.starter.oauth.server.event;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.pat.api.entity.PatUser;
import com.pat.api.mapper.PatUserMapper;
import com.pat.starter.oauth.server.service.PatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import java.util.Date;

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
