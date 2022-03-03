package com.pat.app.poetry.synch.simple;

import com.alibaba.fastjson.JSON;
import com.pat.app.poetry.synch.util.IKAnalyzerUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;


import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * IKAnalyzerTest
 *
 * @author chouway
 * @date 2022.03.03
 */
@Slf4j
public class IKAnalyzerTest {

    @Test
    public void compareA() throws Exception {
        String poetTitle = "度关山";
        String poetAuthor = "曹操";
        String poetTitleAuthorBaike = String.format("%s(%s) - 百度百科",poetTitle,poetAuthor);
        log.info("compare-->poetTitleAuthorBaike={}", poetTitleAuthorBaike);

        List<String> baikeTitles  = new ArrayList<String>();
        baikeTitles.add("度关山(曹操诗作) - 百度百科");
        baikeTitles.add("曹操(游戏《英雄杀》中的武将牌) - 百度百科");
        baikeTitles.add("度关山(王训诗作) - 百度百科");
        baikeTitles.add("曹操·问英雄 - 百度百科");
        baikeTitles.add("度关山(萧纲诗作) - 百度百科");
        log.info("compare-->baikeTitles={}", baikeTitles);

        Map.Entry<String, Double> maxSimilar = IKAnalyzerUtil.maxSimilar(poetTitleAuthorBaike, baikeTitles);
        log.info("compareA-->maxSimilar={}", JSON.toJSONString(maxSimilar));

    }



    @Test
    public void compareB() throws Exception {
        String poetTitle = "却东西门行";
        String poetAuthor = "曹操";
        String poetTitleAuthorBaike = String.format("%s(%s) - 百度百科", poetTitle, poetAuthor);
        log.info("compare-->poetTitleAuthorBaike={}", poetTitleAuthorBaike);

        List<String> baikeTitles = new ArrayList<String>();
        baikeTitles.add("却东西门行(曹操诗作) - 百度百科");
        baikeTitles.add("却东西门行(沈约诗作) - 百度百科");
        baikeTitles.add("新译乐府诗选 - 百度百科");
        baikeTitles.add("边塞诗词赏析 - 百度百科");
        baikeTitles.add("寒蝉赋(《寒蝉赋》杨威) - 百度百科");
        log.info("compare-->baikeTitles={}", baikeTitles);

        Map.Entry<String, Double> maxSimilar = IKAnalyzerUtil.maxSimilar(poetTitleAuthorBaike, baikeTitles);
        log.info("compareA-->maxSimilar={}", JSON.toJSONString(maxSimilar));
    }


}
