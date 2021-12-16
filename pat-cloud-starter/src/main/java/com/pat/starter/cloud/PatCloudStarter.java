package com.pat.starter.cloud;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;

/**
 * AibkCloudStarter
 *
 * @author chouway
 * @date 2021.05.16
 */
@Configuration
@EnableCaching
@PropertySource(value = "classpath:pat-cloud.properties", factory = DefaultPropertySourceFactory.class)
@ImportResource({
        "classpath:/META-INF/spring/pat-provider.xml",
        "classpath:/META-INF/spring/pat-consumer.xml"
})
public class PatCloudStarter {
}
