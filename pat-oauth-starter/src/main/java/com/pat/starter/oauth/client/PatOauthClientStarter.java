package com.pat.starter.oauth.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;

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
}
