package com.pat.app.poetry.synch.simple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * SimpleTest
 *
 * @author chouway
 * @date 2022.02.28
 */
@Slf4j
public class SimpleTest {

    @Test
    public void parse(){
        String source = "[\"a\",\"b\"]";
        JSONArray jsonArray = JSON.parseArray(source);
        for (int i = 0; i < jsonArray.size(); i++) {
            String tem = jsonArray.getString(i);
            log.info("parse-->tem={}", tem);

        }

    }
}
