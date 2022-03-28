package com.pat.api.service.simple;

import cn.hutool.extra.pinyin.PinyinEngine;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.pat.api.bo.PoetInfoBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void replace() {
        String source = "《观沧海》是东汉末年诗人曹操创作的一首四言诗，《步出夏门行》的第一章 [1] 。这首诗是曹操在碣石山登山望海时，用饱蘸浪漫主义激情的大笔，所勾勒出的大海吞吐日月、包蕴万千的壮丽景象；描绘了祖国河山的雄伟壮丽，既刻画了高山大海的壮阔，更表达了诗人以景托志，胸怀天下的进取精神。全诗语言质朴，想象丰富，气势磅礴，苍凉悲壮。";
        String result = source.replaceAll("\\s*\\[\\d+\\]\\s*", "");
        log.info("replace-->result={}", result);

    }

    @Test
    public void hash(){
        Long id = -659759926021223467L;
        id = -944269836329877707L;
        id = 131305859846197836L;
        int result = id.hashCode() % 4 -1;
        log.info("hash-->id={},result={}", id,result);

    }

    @Test
    public void checkHighLight(){
        String source = "明明如月，何时可掇？忧从中来，不可断绝。(明明 一作：佼佼)";
        String highlight = "忧从<em>中</em>来，不可断绝。(明明 一作：佼佼)";
        String replaceHL = source.replaceAll("<em>|</em>", "");
        log.info("replaceAll-->replaceHL={}", replaceHL);
    }

    @Test
    public void dealSpecProkeyVal(){
        String source = "{ 中文名    秋胡行其二}{中文名    秋胡行其二}";
        Pattern compile = Pattern.compile("\\{\\s*(?<key>[\\S]+)\\s+(?<val>[\\S]+)\\s*}");
        Matcher matcher = compile.matcher(source);
        while (matcher.find()) {
            String group = matcher.group();
            log.info("dealSpecProkeyVal-->group={}", group);
            log.info("dealSpecProkeyVal-->key={},val={}", matcher.group("key"),matcher.group("val"));
        }
    }

    /**
     * {"took":3,"timed_out":false,"_shards":{"total":2,"successful":2,"skipped":0,"failed":0},"hits":{"total":{"value":12,"relation":"eq"},"max_score":null,"hits":[]},"aggregations":{"0_vpk_文集":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"曹操诗集","doc_count":12}]},"5_vpk_文学体裁":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"诗歌；乐府诗","doc_count":3},{"key":"五言古诗","doc_count":1},{"key":"四言体","doc_count":1},{"key":"四言诗","doc_count":1},{"key":"词","doc_count":1}]},"4_vpk_作品出处":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"《曹操集》","doc_count":5},{"key":"乐府诗集","doc_count":3},{"key":"善哉行其一","doc_count":1}]},"8_vpk_作品别名":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"短歌行","doc_count":1}]},"2_vpk_作品名称":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"气出唱","doc_count":3},{"key":"善哉行","doc_count":1},{"key":"度关山","doc_count":1},{"key":"短歌行二首","doc_count":1},{"key":"秋胡行其一","doc_count":1},{"key":"蒿里行","doc_count":1},{"key":"观沧海","doc_count":1},{"key":"陌上桑","doc_count":1}]},"9_vpk_朝代":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"魏晋","doc_count":1}]},"6_vpk_作品体裁":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"四言诗","doc_count":2},{"key":"乐府诗","doc_count":1}]},"3_vpk_创作年代":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"东汉","doc_count":6},{"key":"魏晋","doc_count":2},{"key":"三国时期","doc_count":1},{"key":"东汉末年","doc_count":1}]},"1_vpk_作者":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"曹操","doc_count":11}]},"7_vpk_中文名":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"秋胡行其二","doc_count":1}]}}}
     */
    @Test
    public void resolveAggs(){
        String aggs = "{\"took\":3,\"timed_out\":false,\"_shards\":{\"total\":2,\"successful\":2,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":{\"value\":12,\"relation\":\"eq\"},\"max_score\":null,\"hits\":[]},\"aggregations\":{\"0_vpk_文集\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"曹操诗集\",\"doc_count\":12}]},\"5_vpk_文学体裁\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"诗歌；乐府诗\",\"doc_count\":3},{\"key\":\"五言古诗\",\"doc_count\":1},{\"key\":\"四言体\",\"doc_count\":1},{\"key\":\"四言诗\",\"doc_count\":1},{\"key\":\"词\",\"doc_count\":1}]},\"4_vpk_作品出处\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"《曹操集》\",\"doc_count\":5},{\"key\":\"乐府诗集\",\"doc_count\":3},{\"key\":\"善哉行其一\",\"doc_count\":1}]},\"8_vpk_作品别名\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"短歌行\",\"doc_count\":1}]},\"2_vpk_作品名称\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"气出唱\",\"doc_count\":3},{\"key\":\"善哉行\",\"doc_count\":1},{\"key\":\"度关山\",\"doc_count\":1},{\"key\":\"短歌行二首\",\"doc_count\":1},{\"key\":\"秋胡行其一\",\"doc_count\":1},{\"key\":\"蒿里行\",\"doc_count\":1},{\"key\":\"观沧海\",\"doc_count\":1},{\"key\":\"陌上桑\",\"doc_count\":1}]},\"9_vpk_朝代\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"魏晋\",\"doc_count\":1}]},\"6_vpk_作品体裁\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"四言诗\",\"doc_count\":2},{\"key\":\"乐府诗\",\"doc_count\":1}]},\"3_vpk_创作年代\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"东汉\",\"doc_count\":6},{\"key\":\"魏晋\",\"doc_count\":2},{\"key\":\"三国时期\",\"doc_count\":1},{\"key\":\"东汉末年\",\"doc_count\":1}]},\"1_vpk_作者\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"曹操\",\"doc_count\":11}]},\"7_vpk_中文名\":{\"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0,\"buckets\":[{\"key\":\"秋胡行其二\",\"doc_count\":1}]}}}";
        JSONObject aggsJSON = JSON.parseObject(aggs);
        JSONObject aggregations = aggsJSON.getJSONObject("aggregations");
        Map<String, Object> innerMap = aggregations.getInnerMap();
        Map<String, Object> treeMap = new TreeMap<>();
        log.info("resolveAggs-->innerMap={}", innerMap);
        treeMap.putAll(innerMap);
        log.info("resolveAggs-->treeMap={}", JSON.toJSONString(treeMap));


    }


    @Test
    public void replaceAll(){
        String source = "东方朔 《自 悲》";
        log.info("replaceAll-->source={}", source);
        Pattern compile = Pattern.compile("《([^》]+)》");
        Matcher matcher = compile.matcher(source);
        List<String> cleanTitleSpaceStrs = new ArrayList<String>();
        StringBuffer sbf = new StringBuffer();
        while(matcher.find()){
            String group = matcher.group(1);
            log.info("replaceAll-->group={}", group);
            matcher.appendReplacement(sbf," " + group.replaceAll("\\s",""));
        }
        matcher.appendTail(sbf);


        log.info("replaceAll-->sbf={}", sbf);

        String result = source.replaceAll("[^\\u4e00-\\u9fa5_a-zA-Z0-9\\s]+", "");
        log.info("replaceAll-->result={}", result);

    }


    @Test
    public void matchSys(){
        // 生成 Pattern 对象并且编译一个简单的正则表达式"cat"
        Pattern p = Pattern.compile("cat");
        // 用 Pattern 类的 matcher() 方法生成一个 Matcher 对象
        Matcher m = p.matcher("fatcatfatcatfat");
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            //此时sb为fatdogfatdog，cat被替换为dog,并且将最后匹配到之前的子串都添加到sb对象中
            m.appendReplacement(sb,"dog");
            log.info("matchSys-->sb={}", sb);
            
        }
        log.info("matchSys -- -->sb={}", sb);

        //此时sb为fatdogfatdogfat，将最后匹配到后面的子串添加到sb对象中
        m.appendTail(sb);
        //输出内容为fatdogfatdogfat
        System.out.println("sb:"+sb);
    }

    /**
     * es 查询  英文字母
     * 用// //包含表示原串处理
     */
    @Test
    public void abcSpace(){
        String source = "ask god";
        String regex = "([a-zA-Z]+)(\\s+)([a-zA-Z]+)";
        String result = source.replaceAll(regex, "$1\\\\$2\\\\$3");
        log.info("abcSpace-->result={}", result);

    }

    /**
     * es 查询  英文字母
     * 用// //包含表示原串处理
     */
    @Test
    public void excepAbcSpaceDel(){
        String source = "ask god 中 国";
        String regex = "(?<![a-zA-Z])(\\s+)";
        String result = source.replaceAll(regex, "");
        log.info("abcSpace-->result={}", result);

    }

    @Test
    public void pinyin(){
        String source = "我在China";
        log.info("pinyin-->source={}", source);
        source = source.toLowerCase();
        source = source.replaceAll("([a-z])[a-z]+","$1");
        source = source.replaceAll("[^\u4e00-\u9fa5a-z]+", "");
        log.info("pinyin-->source2={}", source);


        PinyinEngine engine = PinyinUtil.getEngine();
        String pinyin = engine.getPinyin(source, "");
        log.info("pinyin-->pinyin={}", pinyin);
        String firstLetter = engine.getFirstLetter(source, "");
        log.info("pinyin-->firstLetter={}", firstLetter);

    }


    @Test
    public void specDeal(){
        String source = "{作品名称 ask god} { 1 作品名称 ask god2 }";
        String regex = "\\{\\s*(?<key>[\\S]+)\\s+(?<val>.*?)\\s*}";
        log.info("specDeal-->source={},regex={}", source,regex);
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(source);
        while (matcher.find()) {
            String key = matcher.group("key");
            String val = matcher.group("val");
            log.info("specDeal-->key={},val={}", key,val);
        }

    }

    @Test
    public void unescapeJson(){
        String source = "{\"id\":\"poet-search-page\",\"params\":{\"aggsInfos\":[{\"aggsName\":\"num_perKey\",\"end\":\"\",\"field\":\"propKeys\",\"size\":8}],\"from\":0,\"hasProps\":true,\"noSources\":true,\"props\":[{\"end\":\"\",\"propKey\":\"篇幅\",\"propVals\":[\"370多句\\\\/近2500字\"]}],\"size\":0}}";
        log.info("unescapeJavaScript-->source={}", source);
        String result = StringEscapeUtils.unescapeJson(source);
        log.info("unescapeJavaScript-->result={}", result);


    }
}
