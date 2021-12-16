package com.pat.starter.mq.service;

import cn.hutool.core.util.IdUtil;
import com.pat.api.exception.BusinessException;
import com.pat.starter.mq.bo.MqMsgBO;
import com.pat.starter.mq.config.RabbitMqData;
import com.pat.starter.mq.constant.MqConstant;
import com.pat.starter.mq.constant.QueueEnum;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * MqSendService
 * @author zhouyw
 * @date 2019.09.18
 */
@Slf4j
public class RabbitMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqData rabbitMqData;
    /**
     * 发送点对点消息
     * @param queueEnum
     * @param msgBO
     * @return
     * @throws BusinessException
     */
    public String directSend(QueueEnum queueEnum, Object msgBO)throws BusinessException{
        checkDirectSend(queueEnum, msgBO);
        MqMsgBO mqMsgBO = new MqMsgBO();
        mqMsgBO.setMessage(msgBO);
        mqMsgBO.setExchange(MqConstant.getExchange(queueEnum));
        mqMsgBO.setRoutingKey(queueEnum.getQueueName());
        String msgId = this.convertAndSend(mqMsgBO);
        log.info("directSend-->msgId={},queueEnum={},msgBO={}", msgId,queueEnum,JSON.toJSONString(msgBO));
        return msgId;
    }

    private void checkDirectSend(QueueEnum queueEnum, Object msgBO) {
        if(queueEnum == null){
            throw new BusinessException("请选择队列");
        }
        if(msgBO == null){
            throw new BusinessException("请上送消息");
        }
        String queueName = queueEnum.getQueueName();
        if (!rabbitMqData.getSendQueues().contains(queueName)) {
            throw new BusinessException("请申明发送消息队列{app.mq.sendQueues}->" + queueEnum);
        }
    }

    private String convertAndSend(MqMsgBO mqMsgBO) throws BusinessException {
        try {
            if (mqMsgBO == null) {
                throw new BusinessException("消息对象为空");
            }
            String exchange = mqMsgBO.getExchange();
            if (ObjectUtils.isEmpty(exchange)) {
                throw new BusinessException("交换器为空");
            }
            String routingKey = mqMsgBO.getRoutingKey();
            if (ObjectUtils.isEmpty(routingKey)) {
                throw new BusinessException("路由Key为空");
            }
            Object message = mqMsgBO.getMessage();
            if(ObjectUtils.isEmpty(message)){
                throw new BusinessException("消息主体为空");
            }
            String msgId = IdUtil.simpleUUID();
            mqMsgBO.setMsgId(msgId);
//          log.debug("mq convertAndSend-->msgId={},mqMessageBO={}",msgId, JSON.toJSONString(mqMessageBO));
            CorrelationData correlationData = new CorrelationData(msgId);
            rabbitTemplate.convertAndSend(exchange,routingKey,mqMsgBO,correlationData);
            return msgId;
        } catch (BusinessException e) {
            log.error("convertAndSend busi error:{}-->[mqMsgBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{mqMsgBO}), e);
            throw e;
        } catch (Exception e) {
            log.error("convertAndSend error:{}-->[mqMsgBO]={}", e.getMessage(), JSON.toJSONString(new Object[]{mqMsgBO}), e);
            throw new BusinessException("系统错误");
        }
    }
}
