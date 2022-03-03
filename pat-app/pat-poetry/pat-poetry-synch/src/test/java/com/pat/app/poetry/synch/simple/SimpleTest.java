package com.pat.app.poetry.synch.simple;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;
import com.pat.api.entity.PoetAuthor;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
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
    public void mustache(){
        String text = "One, two, {{three}}. Three sir!";
        Template tmpl = Mustache.compiler().compile(text);
        Map<String, String> data = new HashMap<String, String>();
        data.put("three", "five");
        log.info("mustache-->render={}", tmpl.execute(data));


        String tmplStr = "{{#poetAuthor}}{{name}}: {{describe}}{{/poetAuthor}}";
        PoetAuthor poetAuthor = new PoetAuthor();
        poetAuthor.setName("曹操");
        poetAuthor.setDescribe("东汉末年，杰出政治家、军事家、诗人。");
        Map<String, Object> dataT = new HashMap<String, Object>();
        dataT.put("poetAuthor", poetAuthor);
        String render = Mustache.compiler().compile(tmplStr).execute(dataT);
        log.info("mustache-->render={}", render);

        tmplStr = "{ {{#join}} {{#poetAuthors}}{{name}}: {{describe}}{{/poetAuthors}} {{/join}} }";
        PoetAuthor poetAuthor1 = new PoetAuthor();
        poetAuthor1.setName("曹操1");
        poetAuthor1.setDescribe("东汉末年，杰出政治家、军事家、诗人。1");
        List<PoetAuthor> poetAuthors= new ArrayList<PoetAuthor>();
        poetAuthors.add(poetAuthor);
        poetAuthors.add(poetAuthor1);
        dataT = new HashMap<String, Object>();
        dataT.put("poetAuthors", poetAuthors);
        render = Mustache.compiler().compile(tmplStr).execute(dataT);
        log.info("mustache-->render={}", render);

    }

    /**
     * mustache允许嵌套
     */
    @Test
    public void mustacheNest(){
        String tmplStr = "{{#a}}{{#b}}{{b}}{{/b}}{{/a}}";
        Map<String, Object> dataT = new HashMap<String, Object>();
//        dataT.put("a", "1");
        dataT.put("b", "b");
        String render = Mustache.compiler().compile(tmplStr).execute(dataT);
        log.info("mustacheNest-->render={}", render);
    }



    @Test
    public void tempA(){
        String SCRIPT_META = "{\"script\":{\"lang\":\"mustache\",\"source\":{%s}}";
        String tempSource = "{\"size\":0,\"aggs\":{{#aggs_infos}}{\"{{aggs_name}}\":{\"terms\":{\"field\":\"{{field}}\",\"size\":{{size}},}{{/aggs_infos}}}";
        String tempContent = String.format(SCRIPT_META,tempSource);
        log.info("tempA-->tempContent={}", tempContent);
        Map<String,List<Map<String,Object>>> runMap = new HashMap<String,List<Map<String,Object>>>();
        List<Map<String,Object>> aggs_infos = new ArrayList<Map<String,Object>>();
        Map<String,Object> aggs_info = new HashMap<String,Object>();
        aggs_info.put("aggs_name","num_per_propKey");
        aggs_info.put("field","propKeys");
        aggs_info.put("size",10);
        aggs_infos.add(aggs_info);
        runMap.put("aggs_infos",aggs_infos);
        String render = Mustache.compiler().compile(tempContent).execute(runMap);
        log.info("tempA-->render={}", render);


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
        JSONArray jsonArray = (JSONArray)JSONPath.eval(jsonObject, "$.aggregations.test.buckets");
        log.info("getObjByJsonPath-->jsonArray={}", JSON.toJSONString(jsonArray));

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
