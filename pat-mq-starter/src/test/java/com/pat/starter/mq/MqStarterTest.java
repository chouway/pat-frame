package com.pat.starter.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * MqStarterTest
 *
 * @author chouway
 * @date 2021.05.13
 */
@SpringBootTest(classes = PatMqApplication.class)
public class MqStarterTest {

    protected Logger log = LoggerFactory.getLogger(this.getClass());
}