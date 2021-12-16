package com.pat.starter.common.game.yys;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * FanWeiTest
 *
 * @author chouway
 * @date 2021.10.19
 */
@Slf4j
public class FanWeiTest {
    /**
     * 生成[0,2]区间的整数
     */
    @Test
    public void random(){
        Random r = new Random();
        int next = r.nextInt(3);
        log.info("random-->next={}", next);
    }

    /**
     * 测试有限反喂目标技能满的概率
     * (2/3)**5 =
     */
    @Test
    public void fanwei(){
        Map<String,Long> cache = new HashMap<String,Long>();
        int asc = 11 ;     // 升级技能次数  总共有三技能    每个技能 最高有5级  默认都是1级
        String matchRegex = "\\d5\\d";
//        String matchRegex = "512";
        if(asc>12){
            throw new RuntimeException("最多只能升12次技能");
        }
        long count = 1000*1000L;//测试次数
        for (long i = 0l; i < count; i++) {
            ShiShen shiShen = new ShiShen();
            for (long j = 0; j < asc; j++) {
               ascBaseRandom(shiShen);
            }
            String key = shiShen.toString();
            Long temC = 0L;
            if (cache.containsKey(key)) {
                temC = cache.get(key);
            }
            cache.put(key, ++temC);
        }
        log.info("fanwei-->cache={}", JSON.toJSONString(cache));

        Long targetC = 0L;



        for (Map.Entry<String, Long> entry : cache.entrySet()) {
            if (entry.getKey().matches(matchRegex)) {
                targetC +=entry.getValue();
                log.info("fanwei-->target.key={},val={}", entry.getKey(),entry.getValue());

            }


        }
        log.info("fanwei-->count={},matchRegex={}", count,matchRegex);
        log.info("fanwei-->gailv={}", new BigDecimal(targetC).divide(new BigDecimal(count)).setScale(5, RoundingMode.CEILING));


    }



    private void ascBaseRandom(ShiShen shiShen){
        int count = 3;


        if(shiShen.getA()==5){
            count--;

        }
        if(shiShen.getB()==5){
            count--;

        }
        if(shiShen.getC()==5){
            count--;

        }
        if(count == 0){
            throw new RuntimeException("无须再升级了");
        }
        int random = new Random().nextInt(count);

        if(count == 1){ //有两满技能
            if(shiShen.getA()<5){
                shiShen.setA(shiShen.getA()+1);
            }else if(shiShen.getB()<5){
                shiShen.setB(shiShen.getB()+1);
            }else if(shiShen.getC()<5) {
                shiShen.setC(shiShen.getC() + 1);
            }
        }else if(count == 2){//有一个满技能了
            if(random==0){
                if(shiShen.getA()<5){
                    shiShen.setA(shiShen.getA()+1);
                }else if(shiShen.getB()<5){
                    shiShen.setB(shiShen.getB()+1);
                }else if(shiShen.getC()<5) {
                    shiShen.setC(shiShen.getC() + 1);
                }
            }else if(random ==1){
                if(shiShen.getC()<5) {
                    shiShen.setC(shiShen.getC() + 1);
                }else if(shiShen.getB()<5){
                    shiShen.setB(shiShen.getB()+1);
                }else if(shiShen.getA()<5){
                    shiShen.setA(shiShen.getA()+1);
                }
            }
        }else if(count == 3) {//没有满技能了
            if(random == 0){
                shiShen.setA(shiShen.getA()+1);
            }else if(random == 1){
                shiShen.setB(shiShen.getB()+1);
            }else if(random == 2){
                shiShen.setC(shiShen.getC()+1);
            }
        }
    }

    /**
     * 需要升三次技能的式神  每个技能 最多升到 5
     */
    private class ShiShen implements Serializable {

        private static final long serialVersionUID = 1L;

        private Integer a = 1;
        private Integer b = 1;
        private Integer c = 1;

        public Integer getA() {
            return a;
        }

        public void setA(Integer a) {
            this.a = a;
        }

        public Integer getB() {
            return b;
        }

        public void setB(Integer b) {
            this.b = b;
        }

        public Integer getC() {
            return c;
        }

        public void setC(Integer c) {
            this.c = c;
        }
        @Override
        public String toString(){
            return String.format("%s%s%s",getA(),getB(),getC());
        }
    }
}
