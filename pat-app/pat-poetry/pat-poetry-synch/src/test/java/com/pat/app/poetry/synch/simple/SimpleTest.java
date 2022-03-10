package com.pat.app.poetry.synch.simple;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
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

    @Test
    public void linkedHashMap(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put("abc","abc");
        map.put("abc","abc2");
        log.info("linkedHashMap-->map={}", JSON.toJSONString(map));
    }

    /**
     * https://hutool.cn/docs/#/extra/拼音/拼音工具-PinyinUtil?id=使用
     * https://hutool.cn/docs/#/extra/%E6%8B%BC%E9%9F%B3/%E6%8B%BC%E9%9F%B3%E5%B7%A5%E5%85%B7-PinyinUtil?id=%e4%bd%bf%e7%94%a8
     */
    @Test
    public void pingying(){
        //1.获取拼音 这里定义的分隔符为空格，你也可以按照需求自定义分隔符，亦或者使用""无分隔符。
        String pinyin = PinyinUtil.getPinyin("建设银行", " ");
        log.info("pingying-->pinyin={}", pinyin);


//        获取拼音首字母
// "h, s, d, y, g"
        String result = PinyinUtil.getFirstLetter("建设银行", ", ");
        log.info("pingying-->result={}", result);


    }

    /**
     * JPinyin是一个汉字转拼音的Java开源类库
     * JPinyin是一个汉字转拼音的Java开源类库，在PinYin4j的功能基础上做了一些改进。
     * 【JPinyin主要特性】
     * 1、准确、完善的字库；
     * Unicode编码从4E00-9FA5范围及3007（〇）的20903个汉字中，JPinyin能转换除46个异体字（异体字不存在标准拼音）之外的所有汉字；
     * 2、拼音转换速度快；
     * 经测试，转换Unicode编码从4E00-9FA5范围的20902个汉字，JPinyin耗时约100毫秒。
     * 3、多拼音格式输出支持；
     * JPinyin支持多种拼音输出格式：带音标、不带音标、数字表示音标以及拼音首字母输出格式；
     * 4、常见多音字识别；
     * JPinyin支持常见多音字的识别，其中包括词组、成语、地名等；
     * 5、简繁体中文转换；
     * 6、支持添加用户自定义字典；
     *
     * String str = "你好世界";
     * PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_MARK); // nǐ,hǎo,shì,jiè
     * PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_NUMBER); // ni3,hao3,shi4,jie4
     * PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
     * PinyinHelper.getShortPinyin(str); // nhsj
     * PinyinHelper.addPinyinDict("user.dict"); // 添加用户自定
     *
     * https://mvnrepository.com/artifact/com.github.stuxuhai/jpinyin
     */
    @Test
    public void pingyinTest(){

        String paragraph = "明明如月，何时可掇？忧从中来，不可断绝。(明明 一作：佼佼)";
        String[] stopSymbol = new String[]{"，","？"};

    }
}
