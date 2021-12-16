package com.pat.starter.mq.listen;


import cn.hutool.extra.spring.SpringUtil;
import com.pat.starter.mq.bo.MqMsgBO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.util.MethodInvokerUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RabbitMqMessageHandle
 *
 * @author chouway
 * @date 2021.05.10
 */
@Slf4j
public class RabbitMqMessageListen implements MessageListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void onMessage(Message message) {
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        MqMsgBO mqMsgBO = (MqMsgBO)messageConverter.fromMessage(message);
        log.info("onMessage str-->msgId={}", mqMsgBO.getMsgId());
        String routingKey = mqMsgBO.getRoutingKey();
        String regex = "queue.(\\w+).(\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(routingKey);
        if(matcher.find()){
           String handleService = matcher.group(1);
           String handleMethod = matcher.group(2);
           Object result = this.handleMsg(mqMsgBO, handleService, handleMethod);
           log.info("onMessage end-->mqMsgBO={},result={}", JSON.toJSONString(mqMsgBO),JSON.toJSONString(result));
        }else{
           log.info("onMessage end-->mqMsgBO={}",JSON.toJSONString(mqMsgBO));
        }

    }

    private Object handleMsg(MqMsgBO mqMsgBO, String handleService, String handleMethod) {
        Object result = null;
        try{
           Object handleServiceBean = SpringUtil.getBean(handleService);
           Object param = mqMsgBO.getMessage();
           Method method = handleServiceBean.getClass().getMethod(handleMethod,param.getClass());
//             log.info("onMessage-->invokeMethod={}", routingKey);
           return ReflectionUtils.invokeMethod(method, handleServiceBean, param);
        }catch (Exception e){
            log.error("消息分发处理失败 error:{}-->[mqMsgBO]={}",e.getMessage(),JSON.toJSONString(new Object[]{mqMsgBO}),e);
           return null;
        }
    }
}
