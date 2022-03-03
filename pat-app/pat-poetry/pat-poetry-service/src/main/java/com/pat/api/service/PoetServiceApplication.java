package com.pat.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PoetServiceApplication
 *
 * @author chouway
 * @date 2022.03.03
 */
@SpringBootApplication
public class PoetServiceApplication {

    private static Logger log = LoggerFactory.getLogger(PoetServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PoetServiceApplication.class, args);
        log.info("poet service started");
    }
}
