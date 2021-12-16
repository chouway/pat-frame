package com.pat.devops.code.generate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AibkCodeGenerateApplication
 *
 * @author zhouyw
 * @date 2021.03.20
 */
@SpringBootApplication
public class PatCodeGenerateApplication {

    private static Logger log = LoggerFactory.getLogger(PatCodeGenerateApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PatCodeGenerateApplication.class, args);
        log.info("code generate started");
    }


}
