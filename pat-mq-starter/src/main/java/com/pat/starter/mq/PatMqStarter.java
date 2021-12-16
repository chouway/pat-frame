package com.pat.starter.mq;

import com.pat.starter.mq.config.RabbitMqConfig;
import com.pat.starter.mq.config.RabbitMqData;
import com.pat.starter.mq.listen.RabbitMqMessageListen;
import com.pat.starter.mq.service.RabbitMqService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;

/**
 * PatMqStarter
 *
 * @author zhouyw
 * @date 2021.05.10
 */
@Configuration
@EnableConfigurationProperties({RabbitMqData.class})
@ConditionalOnProperty(prefix = "app.mq", name = "enabled", havingValue = "true")
@PropertySource(value = "classpath:pat-mq.properties", factory = DefaultPropertySourceFactory.class)
public class PatMqStarter {



    @Bean
    public RabbitMqMessageListen rabbitMqMessageListen(){
        return new RabbitMqMessageListen();
    }

    @Bean
    public RabbitMqService RabbitMqService(){
        return new RabbitMqService();
    }
}
