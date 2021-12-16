package com.pat.starter.cache.annotation;

import java.lang.annotation.*;

/**
 * LzxLockDistributed
 * 分布式锁注解
 *
 * @author zhouyw
 * @date 2021.03.20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LzxLockDistributed {

    String value() default "";

    int time() default 10;
}
