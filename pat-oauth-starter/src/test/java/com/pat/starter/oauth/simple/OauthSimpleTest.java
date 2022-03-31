package com.pat.starter.oauth.simple;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * OauthSimpleTest
 *
 * @author chouway
 * @date 2022.03.31
 */
@Slf4j
public class OauthSimpleTest {

    @Test
    public void bCrypt() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "test";
        log.info("-->rawPassword={}", rawPassword);

        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
        log.info("-->encodedPassword={}", encodedPassword);

        boolean matches = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        log.info("-->matches={}", matches);
    }
}
