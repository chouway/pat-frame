package com.pat.starter.cloud.dubbo.constant;

/**
 * DubboConstant
 *
 * @author chouway
 * @date 2021.05.17
 */
public class DubboConstant {
    private DubboConstant(){

    }
    static{//设置dubbo使用slf4j来记录日志
        System.setProperty("dubbo.application.logger","slf4j");
    }

    public static final String LOG_ID = "logId";
}
