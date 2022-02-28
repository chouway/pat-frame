package com.pat.app.poetry.synch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PcpSynchApplication
 *
 * @author chouway
 * @date 2022.02.25
 */
@SpringBootApplication
public class PoetSynchApplication {

    private static Logger log = LoggerFactory.getLogger(PoetSynchApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PoetSynchApplication.class, args);
        log.info("pcp synch started");
    }
}
