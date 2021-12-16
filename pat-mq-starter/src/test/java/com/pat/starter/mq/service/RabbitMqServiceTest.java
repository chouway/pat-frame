package com.pat.starter.mq.service;

import com.pat.starter.mq.MqStarterTest;
import com.pat.starter.mq.constant.QueueEnum;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RabbitMqServiceTest
 *
 * @author chouway
 * @date 2021.05.10
 */
public class RabbitMqServiceTest extends MqStarterTest {

    @Autowired
    private RabbitMqService rabbitMqService;

    @SneakyThrows
    @Test
    public void directSent() {
        String msgId = rabbitMqService.directSend(QueueEnum.Queue_LockMockService_Process, "abc");
        Thread.sleep(10*1000L);
    }

    @SneakyThrows
    @Test
    public void directSentMuti(){
        int size =10;
        for (int i = 0; i < size; i++) {
            rabbitMqService.directSend(QueueEnum.Queue_LockMockService_Process, "abc");
        }

    }
}