package com.pat.starter.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AibkCommonConfig
 *
 * @author chouway
 * @date 2021.05.13
 */
@Data
@ConfigurationProperties(prefix = "app.common")
public class PatCommonData {
    /**
     * 异常堆栈模版
     */
    public static  final String ERROR_TEMPLATE_FILE = "classpath:template/aibk-error.ftl";


    private Boolean enabled = Boolean.FALSE;
    /**
     *     # 可通过AspectJ expression语法 排除
     *     # aop-service: '!execution(* com.pat.api.service.AibkErrorService.*(..)) and execution(* com.pat.api.service..*.*(..))'
     */
    private String aopService = "execution(* com.pat.starter.common.service..*.*(..))";

    /**
     * 仅在有 IAibkErrorService 实例时执行保存存动作
     * 保存是否异步 默认为否，
     */
    private Boolean errorSaveAsynch = Boolean.FALSE;

    /*
     * 堆栈关键key
     */
    private String stackTraceKey = "com.pat";

}
