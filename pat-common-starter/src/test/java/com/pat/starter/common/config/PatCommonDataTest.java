package com.pat.starter.common.config;

import com.pat.starter.common.CommonStarterTest;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PatCommonDataTest
 *
 * @author chouway
 * @date 2021.05.13
 */
public class PatCommonDataTest extends CommonStarterTest{

    @Autowired
    private PatCommonData patCommonData;

    @Test
    public void data(){
        log.info("data-->patCommonData={}", JSON.toJSONString(patCommonData));
    }
}