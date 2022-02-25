package com.pat.app.pcp.synch;

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
public class PcpSynchApplication {

    private static Logger log = LoggerFactory.getLogger(PcpSynchApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PcpSynchApplication.class, args);
        log.info("pcp synch started");
    }
}
