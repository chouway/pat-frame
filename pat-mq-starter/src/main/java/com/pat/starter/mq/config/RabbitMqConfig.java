package com.pat.starter.mq.config;

import com.pat.starter.mq.PatMqStarter;
import com.pat.starter.mq.constant.MqConstant;
import com.pat.starter.mq.listen.RabbitMqMessageListen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Set;

/**
 * RabbitMqConfig
 *
 * @author chouway
 * @date 2021.05.10
 */
@Slf4j
@Configuration
@AutoConfigureAfter(PatMqStarter.class)
public class RabbitMqConfig {


    @Autowired
    private RabbitMqData rabbitMqData;

    @Autowired
    private RabbitMqMessageListen RabbitMqMessageListen;

    @Bean
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        this.initQueue(rabbitAdmin);
        return rabbitAdmin;
    }

    /**
     * 申明交换机 队列 及绑定key
     *
     * @param rabbitAdmin
     */
    private void initQueue(RabbitAdmin rabbitAdmin) {
        Set<String> sendQueues = rabbitMqData.getSendQueues();
        log.info("initMq-->sendQueues={}", sendQueues);
        if (CollectionUtils.isEmpty(sendQueues)) {
            return;
        }
        MqConstant.checkQueue(sendQueues);
        for (String queueName : sendQueues) {
            DirectExchange aibkDeriectExchange = new DirectExchange(MqConstant.AIBK_DERIECT_EXCHANGE);
            rabbitAdmin.declareExchange(aibkDeriectExchange);
            if (queueName.indexOf("queue") == 0) {
                // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
                // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
                // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
                // 一般设置一下队列的持久化就好,其余两个就是默认false
                Queue queue = new Queue(queueName, true);
                rabbitAdmin.declareQueue(queue);
                String routingKey = queueName;
                rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(aibkDeriectExchange).with(routingKey));
            }
        }
    }

    /**
     * 监听容器
     *
     * @return
     * @throws AmqpException
     * @throws IOException
     */
    @Bean
    public SimpleMessageListenerContainer mqMessageContainer(ConnectionFactory connectionFactory) throws AmqpException, IOException {
        //生成 监听容器
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //设置启动监听超时时间
        container.setConsumerStartTimeout(3000L);
        container.setExposeListenerChannel(true);
        //设置确认模式 设置成自动偷偷懒~
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //监听处理类
        container.setMessageListener(RabbitMqMessageListen);

        //添加监听队列
        Set<String> receiveQueues = rabbitMqData.getReceiveQueues();
        log.info("initMq-->receiveQueues={}", receiveQueues);
        if (!CollectionUtils.isEmpty(receiveQueues)) {
            MqConstant.checkQueue(receiveQueues);
            container.addQueueNames(receiveQueues.toArray(new String[receiveQueues.size()]));
            container.start();
        }
        return container;
    }


}
