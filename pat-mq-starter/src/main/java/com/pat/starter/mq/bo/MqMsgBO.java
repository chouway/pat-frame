package com.pat.starter.mq.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * MqMessageBO
 * mq 消息对象
 * @author zhouyw
 * @date 2019.09.18
 */
@Data
public class MqMsgBO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msgId;

    /**
     * 交互器
     */
    private String exchange;

    /**
     * 路由key
     */
    private String routingKey;

    /**
     * 消息主体
     */
    private T message;
    /**
     * 消息延迟 （毫秒）
     */
    private Long delayMs;

}
