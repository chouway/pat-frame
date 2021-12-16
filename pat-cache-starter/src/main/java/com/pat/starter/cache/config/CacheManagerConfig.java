package com.pat.starter.cache.config;

import com.pat.starter.cache.PatCacheStarter;
import com.pat.starter.cache.constant.CacheEnum;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * CacheManagerConfig
 *
 * https://blog.csdn.net/qinying233/article/details/104689709
 * SpringBoot 2.X版本自定义CacheManager
 *
 * @author chouway
 * @date 2021.03.23
 */
@Configuration
@AutoConfigureAfter(PatCacheStarter.class)
public class CacheManagerConfig {

    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {

        //初始的cache 空间
        CacheEnum[] cacheEnums = CacheEnum.values();
        Set<String> initCacheName = this.getInitCacheNames(cacheEnums);
        Map<String, RedisCacheConfiguration> initCacheConfigs = this.getInitCacheConfigs(cacheEnums);

        //默认的cacheConfig
        RedisCacheConfiguration defCacheConfig = this.getRedisCacheConfiguration(Duration.ofMinutes(15));

        //组装RedisCacheManager: 使用RedisCacheConfiguration创建RedisCacheManager
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .initialCacheNames(initCacheName)
                .withInitialCacheConfigurations(initCacheConfigs)
                .cacheDefaults(defCacheConfig)
                .build();
        return redisCacheManager;
    }

    /**
     * 初始化缓存列对应的配置
     * @param cacheEnums
     * @return
     */
    private Map<String, RedisCacheConfiguration> getInitCacheConfigs(CacheEnum[] cacheEnums) {
        Map<String,RedisCacheConfiguration> initCacheConfigs = new HashMap<String,RedisCacheConfiguration>();
        for (CacheEnum cacheEnum : cacheEnums) {
            initCacheConfigs.put(cacheEnum.getCacheName(),this.getRedisCacheConfiguration(cacheEnum.getDuration()));
        }
        return initCacheConfigs;
    }

    /**
     * 初始化缓存列名
     * @param cacheEnums
     * @return
     */
    private Set<String> getInitCacheNames(CacheEnum[] cacheEnums) {
        Set<String> initCacheName  = new HashSet<String>();
        for (CacheEnum cacheEnum : cacheEnums) {
            initCacheName.add(cacheEnum.getCacheName());
        }
        return initCacheName;
    }

    private RedisCacheConfiguration getRedisCacheConfiguration(Duration duration) {
        return RedisCacheConfiguration.defaultCacheConfig()
                    //设置默认的缓存过期时间
                    .entryTtl(duration)
                    //禁用缓存空值，不缓存null校验
                    .disableCachingNullValues()
                    //指定序列工具 fastjson
//                  .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new FastJsonRedisSerializer<Object>(Object.class)));
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
    }



}
