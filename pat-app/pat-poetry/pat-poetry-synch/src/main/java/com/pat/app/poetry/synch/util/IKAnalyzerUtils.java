package com.pat.app.poetry.synch.util;

import cn.hutool.extra.spring.SpringUtil;
import com.pat.api.constant.EsConstant;
import com.pat.app.poetry.synch.service.es.PoetEsInfoService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * IKAnalyzerUtil
 *
 *  * https://www.bianchengquan.com/article/323634.html
 *  * IKAnalyzer中文分词，计算句子相似度
 *
 * @author chouway
 * @date 2022.03.03
 */
@Slf4j
public class IKAnalyzerUtils {


    /**
     * 返回dsourceStrOther同sourceStr最相似的
     * @param sourceStr
     * @param sourceStrOthers
     * @return
     * @throws Exception
     */
    public static Map.Entry<String, Double> maxSimilar(String sourceStr, Collection<String> sourceStrOthers) throws Exception {
        PoetEsInfoService poetEsInfoService = SpringUtil.getBean(PoetEsInfoService.class);
        Vector<String> sources = poetEsInfoService.participle(sourceStr, EsConstant.ANALYZER_IK_SMART);

        Map<String, Double> similarities = new LinkedHashMap<String, Double>();
        for (String sourceOther : sourceStrOthers) {
            double similarity = getSimilarity(sources, poetEsInfoService.participle(sourceOther, EsConstant.ANALYZER_IK_SMART));
            similarities.put(sourceOther, similarity);
        }
//      log.info("compare-->similarities={}", JSON.toJSONString(similarities));
        Map<String, Double> sortResult = sortMapByValues(similarities);
//      log.info("compare-->sortResult={}", JSON.toJSONString(sortResult));
        Map.Entry<String, Double> entry = sortResult.entrySet().stream().findFirst().get();
        return entry;
    }
    /**
     * 根据Map的 val排序  默认倒排 从大到小
     * @param aMap
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> aMap) {
        HashMap<K, V> finalOut = new LinkedHashMap<>();
        aMap.entrySet()
                .stream()
                .sorted((p1, p2) -> (p2.getValue().compareTo(p1.getValue())))
                .collect(Collectors.toList()).forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
        return finalOut;
    }

    //阈值
    public final static double YUZHI = 0.2 ;

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
