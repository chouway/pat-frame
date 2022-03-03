package com.pat.app.poetry.synch.simple;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;
import com.pat.api.entity.PoetAuthor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        String source = "[\"a\",\"b\",]";
        JSONArray jsonArray = JSON.parseArray(source);
        for (int i = 0; i < jsonArray.size(); i++) {
            String tem = jsonArray.getString(i);
            log.info("parse-->tem={}", tem);

        }

    }


    @Test
    public void json(){
        String source = "{\"a\":\"b\",\"e\":\"f\"}";
        Map<String, String> map = JSON.parseObject(source, Map.class);
        log.info("json-->map={}", map);

    }

    @Test
    @SneakyThrows
    public void readFile(){
        String resourceLocation = "classpath:scripts/simple-aggs.mustache";
        File templateFile = ResourceUtils.getFile(resourceLocation);
        String content = FileUtil.readString(templateFile, StandardCharsets.UTF_8);
        log.info("readFile-->content={}", content);
    }

    @Test
    @SneakyThrows
    public void saveScripts(){
        log.info("readScripts-->method={}", Method.GET.toString());

    }

    /**
      https://www.jb51.net/article/185737.htm
      使用fastjson中的JSONPath处理json数据的方法
     */
    @Test
    public void readByJsonPath(){
        String sourceJson = "{\"took\":2,\"timed_out\":false,\"_shards\":{\"total\":2,\"successful\":2,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":{\"value\":3,\"relation\":\"eq\"},\"max_score\":null,\"hits\":[]},\"aggregations\":{\"test\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"作品体裁\",\"doc_count\":3},{\"key\":\"作品出处\",\"doc_count\":3},{\"key\":\"作品名称\",\"doc_count\":3},{\"key\":\"作者\",\"doc_count\":3},{\"key\":\"创作年代\",\"doc_count\":3},{\"key\":\"作品别名\",\"doc_count\":2}]}}}";

        JSONObject jsonObject = JSON.parseObject(sourceJson);
        Object eval = JSONPath.eval(jsonObject, "$.aggregations.test.buckets.key");
        log.info("getObjByJsonPath-->eval={}", eval);


    }

    @Test
    public void testJsonSource(){
        String sourceJson = "{\n" +
                "  \"metadata\" : {\n" +
                "    \"stored_scripts\" : {\n" +
                "      \"test_2\" : {\n" +
                "        \"lang\" : \"mustache\",\n" +
                "        \"source\" : \"\"\"\n" +
                "    {\n" +
                "      \"size\": 0,\n" +
                "      \"aggs\":  { {{#aggs_infos}}\"{{aggs_name}}\":{\"terms\":{\"field\":\"{{field}}\",\"size\":\"{{size}}\"}} {{end}} {{/aggs_infos}} }\n" +
                "    }\n" +
                "    \"\"\"\n" +
                "      },\n" +
                "      \"test_1\" : {\n" +
                "        \"lang\" : \"mustache\",\n" +
                "        \"source\" : \"\"\"{\"size\":0,\"aggs\":{\"test\":{\"terms\":{\"field\":\"{{field}}\",\"size\":\"{{size}}\"}}}}\"\"\",\n" +
                "        \"options\" : {\n" +
                "          \"content_type\" : \"application/json; charset=UTF-8\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"test_0\" : {\n" +
                "        \"lang\" : \"mustache\",\n" +
                "        \"source\" : \"\"\"{\"size\":0,\"aggs\":{}}\"\"\",\n" +
                "        \"options\" : {\n" +
                "          \"content_type\" : \"application/json; charset=UTF-8\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"test_template\" : {\n" +
                "        \"lang\" : \"mustache\",\n" +
                "        \"source\" : \"\"\"{\"query\":{\"match\":{\"title\":\"{{title}}\"}}}\"\"\",\n" +
                "        \"options\" : {\n" +
                "          \"content_type\" : \"application/json; charset=UTF-8\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        JSONObject jsonObject = JSON.parseObject(sourceJson);
        Object obj = JSONPath.eval(jsonObject, "$.metadata.stored_scripts");
        log.info("testJsonSource-->obj={}", JSON.toJSONString(obj));
    }
}
