package com.pat.starter.common.service;

import com.pat.starter.common.CommonStarterTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AibkCommonMockServiceTest
 *
 * @author chouway
 * @date 2021.05.13
 */
public class PatCommonMockServiceTest extends CommonStarterTest {

    @Autowired
    private PatCommonMockService aibkCommonMockService;

    @Test
    public void mock() {
        String result = aibkCommonMockService.mock("abc");
        log.info("mock-->result={}", result);


    }
}