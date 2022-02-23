package com.pat.simple.es;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PatSimpleEsApplication
 *
 * @author chouway
 * @date 2022.02.23
 */
@SpringBootApplication
public class PatSimpleEsApplication {

    private static Logger log = LoggerFactory.getLogger(PatSimpleEsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PatSimpleEsApplication.class, args);
        log.info("simple es started");
    }
}
