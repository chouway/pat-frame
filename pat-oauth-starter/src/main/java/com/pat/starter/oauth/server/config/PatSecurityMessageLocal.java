package com.pat.starter.oauth.server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * SecurityMessageLocal
 *
 * @author chouway
 * @date 2020.07.09
 */

@Configuration
@ConditionalOnProperty(prefix = "app.oauth.server", name = "enabled", havingValue = "true")
public class PatSecurityMessageLocal {

    @Bean
    public MessageSource messageSource() {
        Locale.setDefault(Locale.CHINA);
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:org/springframework/security/messages_zh_CN");
//      messageSource.addBasenames("classpath:security/messages_zh_CN");
        return messageSource;
    }

}
