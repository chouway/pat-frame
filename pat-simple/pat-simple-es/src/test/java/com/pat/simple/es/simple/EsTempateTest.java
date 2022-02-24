package com.pat.simple.es.simple;

import com.pat.simple.es.PatSimpleEsTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * EsTempateTest
 *
 * https://blog.csdn.net/weixin_42384085/article/details/104662578
 * Elasticsearch(三) Spring Data Elasticsearch
 *
 * @author chouway
 * @date 2022.02.24
 */
public class EsTempateTest extends PatSimpleEsTest {


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void test(){
//        elasticsearchTemplate.deleteIndex()
    }
}
