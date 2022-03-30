package com.pat.poetry.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PoetOauthApplication
 *
 * @author chouway
 * @date 2022.03.30
 */
@SpringBootApplication
public class PoetOauthApplication {

    private static Logger log = LoggerFactory.getLogger(PoetOauthApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PoetOauthApplication.class, args);
        log.info("poet oauth started");
    }
}
