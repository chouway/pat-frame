package com.pat.starter.cache;

import com.pat.starter.cache.aop.MethodLockAop;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * PatCacheStarter
 *
 * @author zhouyw
 * @date 2021.03.20
 */
@Configuration
@EnableCaching
@PropertySource(value = "classpath:pat-cache.properties", factory = DefaultPropertySourceFactory.class)
public class PatCacheStarter {

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory){
        return new RedisLockRegistry(redisConnectionFactory,"aibk-cloud");
    }

    @Bean
    public MethodLockAop methodLockAop(RedisLockRegistry redisLockRegistry){
        return new MethodLockAop(redisLockRegistry);
    }




}
