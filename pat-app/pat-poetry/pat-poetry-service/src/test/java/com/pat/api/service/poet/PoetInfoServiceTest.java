package com.pat.api.service.poet;

import com.alibaba.fastjson.JSON;
import com.pat.api.bo.PoetBaikeBO;
import com.pat.api.service.PoetServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PoetInfoServiceTest
 *
 * @author chouway
 * @date 2022.03.21
 */
public class PoetInfoServiceTest extends PoetServiceTest {

    @Autowired
    private IPoetInfoService poetInfoService;

    @Test
    public void getBaikeById() {
        PoetBaikeBO poetBaikeBO = poetInfoService.getBaikeById(2L);
        log.info("getBaikeById-->poetBaikeBO={}", JSON.toJSONString(poetBaikeBO));
    }


}