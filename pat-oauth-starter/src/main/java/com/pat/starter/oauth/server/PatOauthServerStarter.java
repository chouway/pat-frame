package com.pat.starter.oauth.server;

import com.pat.starter.oauth.server.service.PatUserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;

/**
 * PatOauthServerStarter
 *
 * @author chouway
 * @date 2022.03.29
 */
@Configuration
@ConditionalOnProperty(prefix = "app.oauth.server", name = "enabled", havingValue = "true")
@PropertySource(value = "classpath:pat-oauth-server.properties", factory = DefaultPropertySourceFactory.class)
public class PatOauthServerStarter {

    @Bean
    public PatUserService UserDetailsService(){
        return new PatUserService();
    }
}
