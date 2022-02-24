package com.pat.simple.es.service;

import com.pat.simple.es.PatSimpleEsTest;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * PatEsRestServiceTest
 *
 * kibana
 * http://192.168.40.254:5602/
 * elasticsearch
 * http://192.168.40.254:9201/
 *
 * 输入上方配置的用户名密码：elastic和w12345
 *
 * @author chouway
 * @date 2022.02.23
 */
public class PatEsRestServiceTest extends PatSimpleEsTest {

    @Autowired
    private PatEsRestService patEsRestService;

    /**
     * index name  不可包含以下字符 [ , \", *, \\, <, |, ,, >, /, ?]
     * 建
     */
    @Test
    void indexAsync() {
        IndexRequest indexRequest = new IndexRequest().index("get-together_group").type("_create").id("1");
        Map<String,Object> docMap = new HashMap<String,Object>();
        docMap.put("name","Elasticsearch Denver");
        docMap.put("organizer","Lee");
        log.info("indexAsync-->docMap={}", docMap);
        indexRequest.source(docMap);
        patEsRestService.indexAsync(indexRequest);
    }

    @AfterAll
    public static void sleep() throws InterruptedException {
        Long time = 10L;
        log.info("sleep str-->time={} s", time);
        Thread.sleep(time*1000L);
        log.info("sleep end-->time={} s", time);
    }
}