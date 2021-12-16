package com.pat.starter.cache.constant;

import java.time.Duration;

/**
 * CacheEnum
 * 缓存名称
 *
 * 注解Cacheable 需要指明 cacheName 使用哪个存储空间  在redis上表现就是 cacheName:cacheKey
 *
 * @author chouway
 * @date 2021.03.23
 */
public enum CacheEnum {

    HOUR(CacheConstant.HOUR,Duration.ofHours(1)),
    HOUR2(CacheConstant.HOUR2,Duration.ofHours(2)),
    DAY(CacheConstant.DAY,Duration.ofDays(1)),
    WEEK(CacheConstant.WEEK,Duration.ofDays(7)),
    MONTH(CacheConstant.MONTH,Duration.ofDays(30));


    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 过期时间
     */
    private Duration duration;

    private CacheEnum(String cacheName, Duration duration) {
        this.cacheName = cacheName;
        this.duration = duration;
    }

    public String getCacheName() {
        return cacheName;
    }

    public Duration getDuration() {
        return duration;
    }
}
