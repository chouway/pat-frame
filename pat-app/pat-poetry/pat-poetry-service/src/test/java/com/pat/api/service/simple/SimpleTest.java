package com.pat.api.service.simple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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

    /**
     * {"_index":"poet-info_v0","_id":"2","_version":2,"_seq_no":19,"_primary_term":1,"found":true,"_source":{"_class":"com.pat.app.poetry.synch.eo.PoetInfoEO","id":2,"title":"短歌行 其一","content":"对酒当歌，人生几何！譬如朝露，去日苦多。慨当以慷，忧思难忘。何以解忧？唯有杜康。青青子衿，悠悠我心。但为君故，沉吟至今。呦呦鹿鸣，食野之苹。我有嘉宾，鼓瑟吹笙。明明如月，何时可掇？忧从中来，不可断绝。(明明 一作：佼佼)越陌度阡，枉用相存。契阔谈讌，心念旧恩。(谈讌 一作：谈宴)月明星稀，乌鹊南飞。绕树三匝，何枝可依？山不厌高，海不厌深。周公吐哺，天下归心。(海 一作：水)","baikeDesc":"《短歌行二首》，是东汉末年政治家、文学家曹操以乐府古题创作的两首诗。第一首诗通过宴会的歌唱，以沉稳顿挫的笔调抒写诗人求贤如渴的思想感情和统一天下的雄心壮志；第二首诗借礼赞周文王、齐桓公、晋文公坚守臣节的史事，申明自己只有扶佐汉室之志，决无代汉自立之心。两诗珠联璧合，庄重典雅，内容深厚，感情充沛，其政治内容和意义完全熔铸于浓郁的抒情意境中，全面展现了曹操的人格、学养、抱负和理想，充分显示了其雄深雅健的诗品。","author":"曹操","propKeys":["文集","作品名称","作者","创作年代","作品出处","作品体裁","作品别名"],"properties":{"文集":"曹操诗集","作品名称":"短歌行二首","作者":"曹操","创作年代":"东汉","作品出处":"《曹操集》","作品体裁":"四言诗","作品别名":"短歌行"},"index":1,"count":0}}
     */
    @Test
    public void resovle(){
        String source  = "{\"_index\":\"poet-info_v0\",\"_id\":\"2\",\"_version\":2,\"_seq_no\":19,\"_primary_term\":1,\"found\":true,\"_source\":{\"_class\":\"com.pat.app.poetry.synch.eo.PoetInfoEO\",\"id\":2,\"title\":\"短歌行 其一\",\"content\":\"对酒当歌，人生几何！譬如朝露，去日苦多。慨当以慷，忧思难忘。何以解忧？唯有杜康。青青子衿，悠悠我心。但为君故，沉吟至今。呦呦鹿鸣，食野之苹。我有嘉宾，鼓瑟吹笙。明明如月，何时可掇？忧从中来，不可断绝。(明明 一作：佼佼)越陌度阡，枉用相存。契阔谈讌，心念旧恩。(谈讌 一作：谈宴)月明星稀，乌鹊南飞。绕树三匝，何枝可依？山不厌高，海不厌深。周公吐哺，天下归心。(海 一作：水)\",\"baikeDesc\":\"《短歌行二首》，是东汉末年政治家、文学家曹操以乐府古题创作的两首诗。第一首诗通过宴会的歌唱，以沉稳顿挫的笔调抒写诗人求贤如渴的思想感情和统一天下的雄心壮志；第二首诗借礼赞周文王、齐桓公、晋文公坚守臣节的史事，申明自己只有扶佐汉室之志，决无代汉自立之心。两诗珠联璧合，庄重典雅，内容深厚，感情充沛，其政治内容和意义完全熔铸于浓郁的抒情意境中，全面展现了曹操的人格、学养、抱负和理想，充分显示了其雄深雅健的诗品。\",\"author\":\"曹操\",\"propKeys\":[\"文集\",\"作品名称\",\"作者\",\"创作年代\",\"作品出处\",\"作品体裁\",\"作品别名\"],\"properties\":{\"文集\":\"曹操诗集\",\"作品名称\":\"短歌行二首\",\"作者\":\"曹操\",\"创作年代\":\"东汉\",\"作品出处\":\"《曹操集》\",\"作品体裁\":\"四言诗\",\"作品别名\":\"短歌行\"},\"index\":1,\"count\":0}}";



    }
}
