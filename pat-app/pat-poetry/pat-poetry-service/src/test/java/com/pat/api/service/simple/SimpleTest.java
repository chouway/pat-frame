package com.pat.api.service.simple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * SimpleTest
 *
 * @author chouway
 * @date 2022.03.14
 */
@Slf4j
public class SimpleTest {

    @Test
    public void resolveSearch(){
        String searchResult = "{\"took\":5,\"timed_out\":false,\"_shards\":{\"total\":2,\"successful\":2,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":{\"value\":2,\"relation\":\"eq\"},\"max_score\":null,\"hits\":[{\"_index\":\"poet-info_v0\",\"_id\":\"1\",\"_score\":15.677839,\"_source\":{},\"sort\":[15.677839,0]},{\"_index\":\"poet-info_v0\",\"_id\":\"17\",\"_score\":2.5348787,\"_source\":{},\"sort\":[2.5348787,16]}]},\"aggregations\":{\"num_perKey\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"作品名称\",\"doc_count\":2},{\"key\":\"作者\",\"doc_count\":2},{\"key\":\"创作年代\",\"doc_count\":2},{\"key\":\"文集\",\"doc_count\":2},{\"key\":\"作品体裁\",\"doc_count\":1},{\"key\":\"作品出处\",\"doc_count\":1},{\"key\":\"文学体裁\",\"doc_count\":1}]}}}";
        JSONObject searchJson = JSON.parseObject(searchResult);
        Integer totalVal = (Integer) JSONPath.eval(searchJson, "/hits/total/value");
        log.info("resolveSearch-->totalVal={}", totalVal);
        Object ids = (Object)JSONPath.eval(searchJson, "/hits/hits/_id");
        log.info("resolveSearch-->ids={}", ids);
        JSONArray buckets = (JSONArray)JSONPath.eval(searchJson, "/aggregations/num_perKey/buckets");
        log.info("resolveSearch-->buckets={}", JSON.toJSONString(buckets));



    }

    @Test
    public void log() {

    }
}
