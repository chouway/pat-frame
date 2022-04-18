package com.pat.starter.oauth.client;

import com.pat.starter.oauth.common.service.PatResourceService;
import com.pat.starter.oauth.common.service.PatUserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PatOauthStarter
 *
 * @author chouway
 * @date 2022.03.29
 */
@Configuration
@ConditionalOnProperty(prefix = "app.oauth.client", name = "enabled", havingValue = "true")
@PropertySource(value = "classpath:pat-oauth-client.properties", factory = DefaultPropertySourceFactory.class)
public class PatOauthClientStarter {

    @Bean
    public PatResourceService patResourceService(){
        return new PatResourceService();
    }

    @Bean
    public PatUserService patUserService() {
        return new PatUserService();
    }

    /**
     * 配置密码加密对象（解密时会用到PasswordEncoder的matches判断是否正确）
     * 用户的password和客户端clientSecret用到，所以存的时候存该bean encode过的密码
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
