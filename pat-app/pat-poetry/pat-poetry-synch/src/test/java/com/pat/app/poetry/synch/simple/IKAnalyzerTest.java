package com.pat.app.poetry.synch.simple;

import com.alibaba.fastjson.JSON;
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
 * https://www.bianchengquan.com/article/323634.html
 * IKAnalyzer中文分词，计算句子相似度
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
        String poetTitleAuthorBaike = String.format("%s%s百度百科",poetTitle,poetAuthor);
        log.info("compare-->poetTitleAuthorBaike={}", poetTitleAuthorBaike);

        List<String> baikeTitles  = new ArrayList<String>();
        baikeTitles.add("度关山(曹操诗作) - 百度百科");
        baikeTitles.add("曹操(游戏《英雄杀》中的武将牌) - 百度百科");
        baikeTitles.add("度关山(王训诗作) - 百度百科");
        baikeTitles.add("曹操·问英雄 - 百度百科");
        baikeTitles.add("度关山(萧纲诗作) - 百度百科");
        log.info("compare-->baikeTitles={}", baikeTitles);

        Vector<String> source = participle(poetTitleAuthorBaike);

        Map<String,Double> similarities = new LinkedHashMap<String, Double>();
        for (String baikeTitle : baikeTitles) {
                double similarity = getSimilarity(source, participle(baikeTitle));
                similarities.put(baikeTitle,similarity);
        }
        log.info("compare-->similarities={}", JSON.toJSONString(similarities));
        Map<String, Double> sortResult = sortMapByValues(similarities);
        log.info("compare-->sortResult={}", JSON.toJSONString(sortResult));
        String key = sortResult.entrySet().stream().findFirst().get().getKey();
        log.info("compare-->key={}", key);
    }

    @Test
    public void compareB() throws Exception {
        String poetTitle = "却东西门行";
        String poetAuthor = "曹操";
        String poetTitleAuthorBaike = String.format("%s%s百度百科",poetTitle,poetAuthor);
        log.info("compare-->poetTitleAuthorBaike={}", poetTitleAuthorBaike);

        List<String> baikeTitles  = new ArrayList<String>();
        baikeTitles.add("却东西门行(曹操诗作) - 百度百科");
        baikeTitles.add("却东西门行(沈约诗作) - 百度百科");
        baikeTitles.add("新译乐府诗选 - 百度百科");
        baikeTitles.add("边塞诗词赏析 - 百度百科");
        baikeTitles.add("寒蝉赋(《寒蝉赋》杨威) - 百度百科");
        log.info("compare-->baikeTitles={}", baikeTitles);

        Vector<String> source = participle(poetTitleAuthorBaike);

        Map<String,Double> similarities = new LinkedHashMap<String, Double>();
        for (String baikeTitle : baikeTitles) {
            double similarity = getSimilarity(source, participle(baikeTitle));
            similarities.put(baikeTitle,similarity);
        }
        log.info("compare-->similarities={}", JSON.toJSONString(similarities));
        Map<String, Double> sortResult = sortMapByValues(similarities);
        log.info("compare-->sortResult={}", JSON.toJSONString(sortResult));
        String key = sortResult.entrySet().stream().findFirst().get().getKey();
        log.info("compare-->key={}", key);
    }
    public static <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> aMap) {
        HashMap<K, V> finalOut = new LinkedHashMap<>();
        aMap.entrySet()
                .stream()
                .sorted((p1, p2) -> (p2.getValue().compareTo(p1.getValue())))
                .collect(Collectors.toList()).forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
        return finalOut;
    }

    public static Vector<String> participle( String str ) {

        Vector<String> str1 = new Vector<String>() ;//对输入进行分词

        try {

            StringReader reader = new StringReader( str );
            IKSegmenter ik = new IKSegmenter(reader,true);//当为true时，分词器进行最大词长切分
            Lexeme lexeme = null ;

            while( ( lexeme = ik.next() ) != null ) {
                str1.add( lexeme.getLexemeText() );
            }

            if( str1.size() == 0 ) {
                return null ;
            }

            //分词后
            //System.out.println( "str分词后：" + str1 );

        } catch (Exception e1 ) {
            System.out.println();
        }
        return str1;
    }

    //阈值
    public static double YUZHI = 0.2 ;

    /**
     * 返回百分比
     * @author: Administrator
     * @Date: 2015年1月22日
     * @param T1
     * @param T2
     * @return
     */
    public static double getSimilarity(Vector<String> T1, Vector<String> T2) throws Exception {
        int size = 0 , size2 = 0 ;
        if ( T1 != null && ( size = T1.size() ) > 0 && T2 != null && ( size2 = T2.size() ) > 0 ) {

            Map<String, double[]> T = new HashMap<String, double[]>();

            //T1和T2的并集T
            String index = null ;
            for ( int i = 0 ; i < size ; i++ ) {
                index = T1.get(i) ;
                if( index != null){
                    double[] c = T.get(index);
                    c = new double[2];
                    c[0] = 1;	//T1的语义分数Ci
                    c[1] = YUZHI;//T2的语义分数Ci
                    T.put( index, c );
                }
            }

            for ( int i = 0; i < size2 ; i++ ) {
                index = T2.get(i) ;
                if( index != null ){
                    double[] c = T.get( index );
                    if( c != null && c.length == 2 ){
                        c[1] = 1; //T2中也存在，T2的语义分数=1
                    }else {
                        c = new double[2];
                        c[0] = YUZHI; //T1的语义分数Ci
                        c[1] = 1; //T2的语义分数Ci
                        T.put( index , c );
                    }
                }
            }

            //开始计算，百分比
            Iterator<String> it = T.keySet().iterator();
            double s1 = 0 , s2 = 0, Ssum = 0;  //S1、S2
            while( it.hasNext() ){
                double[] c = T.get( it.next() );
                Ssum += c[0]*c[1];
                s1 += c[0]*c[0];
                s2 += c[1]*c[1];
            }
            //百分比
            return Ssum / Math.sqrt( s1*s2 );
        } else {
            throw new Exception("传入参数有问题！");
        }
    }
}
