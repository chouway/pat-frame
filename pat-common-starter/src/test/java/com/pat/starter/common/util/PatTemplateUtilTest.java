package com.pat.starter.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AibkTemplateUtilTest
 *
 * @author chouway
 * @date 2021.05.18
 */
@Slf4j
class PatTemplateUtilTest {

    @Test
    void getContentFromFile() {
        String resourceLocation = "classpath:template/pat-error.ftl";
        String content = PatTemplateUtil.getContentFromFile(resourceLocation);
        log.info("getContentFromFile-->content={}", content);

    }
}