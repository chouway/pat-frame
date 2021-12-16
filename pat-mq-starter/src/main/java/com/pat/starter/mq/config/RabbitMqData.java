package com.pat.starter.mq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;

import java.util.Set;

/**
 * RabbitMqData
 *
 * @author chouway
 * @date 2021.05.10
 */
@Data
@ConfigurationProperties(prefix = "app.mq")
public class RabbitMqData {

    private Boolean enabled = Boolean.FALSE;

    private Set<String> sendQueues;

    private Set<String> receiveQueues;
}
