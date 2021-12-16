package com.pat.starter.common.game.yys;

import com.alibaba.fastjson.JSON;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * ZixuanTest
 *
 *
 * https://echarts.apache.org/examples/zh/editor.html?c=line-smooth
 * 通过echarts 生成表格数据
 *
 *
 * @author chouway
 * @date 2021.10.20
 */
@Slf4j
public class ZixuanTest {

    /**
     * 非真全图
     * 初始0抽时   一主选 10% 三副选 5%
     * 到100抽时   一主选 12% 三副选 6%
     * 到200抽时   一主选 16% 三副选 7%
     * 到300抽时   一主选 20% 三副选 8%
     * 到400抽时   一主选 30% 三副选 9%
     * 到500抽时   一主选 50% 三副选 10%
     * 到600抽时   一主选  70% 三副选 10%
     * 主最多享受一次加成 副最多二次加成
     * 默认ssr/sp抽卡概率为 1.25%
     * 这次活动 前59抽没抽到 下一次必出ssr/sp
     * 前699未出主选 ，则700抽必出。
     *
     * 目标 统计 第一次出自选的次数及分布概率
     */
    @Test
    public void zixuan(){
        this.percent(100);

    }

    private BigDecimal percent(Integer targetGet){
        long count = 100*1000L;
        int maxC  = 700;//限最多700抽
//        int targetGet = 10;//targetGet  目标抽数内获取主选的概率
        Map<String,Integer> cache = new HashMap<String,Integer>();
        for (int i = 0; i < count; i++) {

            RollInfo rollInfo = new RollInfo();
            int main = 0;
            int sub = 0;
            for (int j = 0; j < maxC; j++) {
                String nextRoll = rollInfo.getNextRoll();
                if(MAIN.equals(nextRoll)){
                    main++;
                }else if(SUB.equals(nextRoll)){
                    sub++;
                }
                if(rollInfo.isEnd()){
                    break;
                }
            }
            String key = String.format("%s%s_%s",main,sub,rollInfo.getIndex());
            Integer value = 0;
            if(cache.containsKey(key)){
                value = cache.get(key);
            }
            cache.put(key, ++value);
        }

        //统计结果 160以内抽出 主选的概率是多少
        int targetCount = 0;

        Map<String,Integer> targetCache = new HashMap<String,Integer>();
        for (Map.Entry<String, Integer> entry : cache.entrySet()) {
            String key = entry.getKey();
            String index = key.split("_")[1];
            if(Integer.parseInt(index)<=targetGet){
                Integer value = entry.getValue();
                targetCount+= value;
                targetCache.put(key, value);
            }

        }
        log.info("zixuan-->cache={}", JSON.toJSONString(cache));
        log.info("zixuan-->targetCache={}", JSON.toJSONString(targetCache));
        BigDecimal percent = new BigDecimal(targetCount).divide(new BigDecimal(count)).setScale(5, RoundingMode.CEILING);
        log.info("zixuan-->targetGet={},targetCount={},count={},percent={}", targetGet,targetCount,count, percent);
        return percent;
    }



    @Test
    public void average(){
        int step =5;
        int max = 700;
        int min = 0;
        Map<Integer, BigDecimal> map = new LinkedHashMap<Integer, BigDecimal>();
        for (int i = 1; i < 141; i++) {
            int temV = step * i;
            BigDecimal percent = this.percent(temV);
            map.put(temV,percent);
        }
        log.info("average-->map={}", JSON.toJSONString(map));

    }

    @Test
    public void maxProfit(){
        String jsonStr = "{\n" +
                "\"5\": 0.00626,\n" +
                "\"10\": 0.0127,\n" +
                "\"15\": 0.01845,\n" +
                "\"20\": 0.02478,\n" +
                "\"25\": 0.03187,\n" +
                "\"30\": 0.03657,\n" +
                "\"35\": 0.04182,\n" +
                "\"40\": 0.04898,\n" +
                "\"45\": 0.05632,\n" +
                "\"50\": 0.06083,\n" +
                "\"55\": 0.06699,\n" +
                "\"60\": 0.11982,\n" +
                "\"65\": 0.12827,\n" +
                "\"70\": 0.1357,\n" +
                "\"75\": 0.14296,\n" +
                "\"80\": 0.15181,\n" +
                "\"85\": 0.15862,\n" +
                "\"90\": 0.16713,\n" +
                "\"95\": 0.17433,\n" +
                "\"100\": 0.18193,\n" +
                "\"105\": 0.19173,\n" +
                "\"110\": 0.19853,\n" +
                "\"115\": 0.20973,\n" +
                "\"120\": 0.24322,\n" +
                "\"125\": 0.25295,\n" +
                "\"130\": 0.26131,\n" +
                "\"135\": 0.27096,\n" +
                "\"140\": 0.27995,\n" +
                "\"145\": 0.29076,\n" +
                "\"150\": 0.30066,\n" +
                "\"155\": 0.30849,\n" +
                "\"160\": 0.31739,\n" +
                "\"165\": 0.32294,\n" +
                "\"170\": 0.33562,\n" +
                "\"175\": 0.34612,\n" +
                "\"180\": 0.36233,\n" +
                "\"185\": 0.37032,\n" +
                "\"190\": 0.38,\n" +
                "\"195\": 0.38715,\n" +
                "\"200\": 0.39621,\n" +
                "\"205\": 0.40894,\n" +
                "\"210\": 0.41782,\n" +
                "\"215\": 0.42906,\n" +
                "\"220\": 0.44001,\n" +
                "\"225\": 0.45475,\n" +
                "\"230\": 0.46357,\n" +
                "\"235\": 0.47015,\n" +
                "\"240\": 0.48908,\n" +
                "\"245\": 0.4999,\n" +
                "\"250\": 0.50652,\n" +
                "\"255\": 0.5136,\n" +
                "\"260\": 0.52551,\n" +
                "\"265\": 0.53689,\n" +
                "\"270\": 0.54603,\n" +
                "\"275\": 0.55216,\n" +
                "\"280\": 0.56051,\n" +
                "\"285\": 0.56759,\n" +
                "\"290\": 0.57701,\n" +
                "\"295\": 0.58395,\n" +
                "\"300\": 0.59525,\n" +
                "\"305\": 0.60459,\n" +
                "\"310\": 0.61692,\n" +
                "\"315\": 0.62852,\n" +
                "\"320\": 0.63351,\n" +
                "\"325\": 0.64443,\n" +
                "\"330\": 0.64898,\n" +
                "\"335\": 0.66115,\n" +
                "\"340\": 0.66704,\n" +
                "\"345\": 0.67764,\n" +
                "\"350\": 0.6837,\n" +
                "\"355\": 0.69332,\n" +
                "\"360\": 0.70036,\n" +
                "\"365\": 0.70947,\n" +
                "\"370\": 0.71753,\n" +
                "\"375\": 0.72251,\n" +
                "\"380\": 0.73344,\n" +
                "\"385\": 0.73899,\n" +
                "\"390\": 0.74481,\n" +
                "\"395\": 0.75182,\n" +
                "\"400\": 0.75569,\n" +
                "\"405\": 0.76768,\n" +
                "\"410\": 0.77561,\n" +
                "\"415\": 0.78336,\n" +
                "\"420\": 0.79294,\n" +
                "\"425\": 0.80096,\n" +
                "\"430\": 0.80941,\n" +
                "\"435\": 0.81732,\n" +
                "\"440\": 0.82487,\n" +
                "\"445\": 0.8314,\n" +
                "\"450\": 0.83767,\n" +
                "\"455\": 0.84598,\n" +
                "\"460\": 0.84986,\n" +
                "\"465\": 0.85742,\n" +
                "\"470\": 0.86227,\n" +
                "\"475\": 0.86732,\n" +
                "\"480\": 0.87252,\n" +
                "\"485\": 0.87847,\n" +
                "\"490\": 0.88071,\n" +
                "\"495\": 0.886,\n" +
                "\"500\": 0.89259,\n" +
                "\"505\": 0.89818,\n" +
                "\"510\": 0.90606,\n" +
                "\"515\": 0.91085,\n" +
                "\"520\": 0.9164,\n" +
                "\"525\": 0.92314,\n" +
                "\"530\": 0.92824,\n" +
                "\"535\": 0.93341,\n" +
                "\"540\": 0.93925,\n" +
                "\"545\": 0.94315,\n" +
                "\"550\": 0.94917,\n" +
                "\"555\": 0.95342,\n" +
                "\"560\": 0.95524,\n" +
                "\"565\": 0.95924,\n" +
                "\"570\": 0.96067,\n" +
                "\"575\": 0.96363,\n" +
                "\"580\": 0.96644,\n" +
                "\"585\": 0.96938,\n" +
                "\"590\": 0.97263,\n" +
                "\"595\": 0.97352,\n" +
                "\"600\": 0.97633,\n" +
                "\"605\": 0.9781,\n" +
                "\"610\": 0.98147,\n" +
                "\"615\": 0.98277,\n" +
                "\"620\": 0.98446,\n" +
                "\"625\": 0.98607,\n" +
                "\"630\": 0.98802,\n" +
                "\"635\": 0.98882,\n" +
                "\"640\": 0.99126,\n" +
                "\"645\": 0.99168,\n" +
                "\"650\": 0.99277,\n" +
                "\"655\": 0.99393,\n" +
                "\"660\": 0.99439,\n" +
                "\"665\": 0.99541,\n" +
                "\"670\": 0.99586,\n" +
                "\"675\": 0.99629,\n" +
                "\"680\": 0.99672,\n" +
                "\"685\": 0.99691,\n" +
                "\"690\": 0.99775,\n" +
                "\"695\": 0.99748,\n" +
                "\"700\": 1.0\n" +
                "}";

        Map<String, BigDecimal> map = JSON.parseObject(jsonStr, Map.class);

        Map.Entry<String, BigDecimal> temEntry=null;
        BigDecimal temBD = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            log.info("maxProfit-->entry.getKey={}", entry.getKey());

            BigDecimal result = new BigDecimal(entry.getValue().toString()).divide(new BigDecimal(entry.getKey().toString()),5,RoundingMode.CEILING);
            if (temBD.compareTo(result)<1) {
                temBD = result;
                temEntry = entry;
            }
        }
        log.info("maxProfit-->temEntry={}", JSON.toJSONString(temEntry));

    }

    //ssr/p  是主选  2 是副选 1  非自选 0   非ssrp  -1
    public static final String MAIN = "2";

    public static final String SUB = "1";

    public static final String NOT = "0";

    public static final String NULL = "-1";



    public class RollInfo implements Serializable{

        private static final long serialVersionUID = 1L;
        //当前已抽卡数
        private Integer index = 0;
        //当前未获取数
        private Integer noHave = 0;
        //当前主选 up数
        private Integer mainUp = 1;
        //当前主选 up基数
        private Integer mainBase = 10;
        //当前副选 up数 但只能生效两个
        private Integer subUp = 3;
        //当前副选 up基数
        private Integer subBase = 5;

        public String getNextRoll(){
            String rollType = rollType();
            dealRoll(rollType);
            return rollType;
        }

        /**
         * 主选出则停止
         * @return
         */
        public boolean isEnd(){
            return mainUp==0;
        }

        private void dealRoll(String rollType){
            if(MAIN.equals(rollType)){
                mainUp--;
                noHave = 0;
            }else if(SUB.equals(rollType)){
                subUp--;
                noHave = 0;
            }else if(NOT.equals(rollType)){
                noHave = 0;
            }else{
                noHave++;
            }
            index++;
            if(index < 100){
                return;
            }else if(index >= 100 && index < 200){
                if(mainUp>0){
                    mainBase = 12;
                }
                if(subUp>0){
                    subBase = 6;
                }
            }else if(index >=200 && index <300){
                if(mainUp>0){
                    mainBase = 16;
                }
                if(subUp>0){
                    subBase = 7;
                }
            }else if(index >=300 && index <400){
                if(mainUp>0){
                    mainBase = 20;
                }
                if(subUp>0){
                    subBase = 8;
                }
            }else if(index >=400 && index <500){
                if(mainUp>0){
                    mainBase = 30;
                }
                if(subUp>0){
                    subBase = 9;
                }
            }else if(index >=500 && index <600){
                if(mainUp>0){
                    mainBase = 50;
                }
                if(subUp>0){
                    subBase = 10;
                }
            }else if(index >=600 && index <700){
                if(mainUp>0){
                    mainBase = 70;
                }
                if(subUp>0){
                    subBase = 10;
                }
            }
        }

        private String rollType(){
            if(mainUp>0&&index==699){//700必出主选
                return MAIN;
            }
            boolean isRollSSRP = false;
            if(noHave==59){
                //前59次都没有抽出 则该次抽卡必出
                isRollSSRP = true;
            }else{
                //ssr/p 出卡概率 1.25%
                int random = new Random().nextInt(10000);
                if(random<125){ //出ssr/p
                    isRollSSRP = true;
                }
            }

            if(isRollSSRP){
                int random = new Random().nextInt(100);
                if(mainUp>0){//主选 up还在
                    if(random<mainBase){//主选
                        return  MAIN;
                    }
                    if(random>=mainBase&&random<mainBase+subUp*subBase){//副选
                        return SUB;
                    }
                }
                if(subUp>1){// 主选不在 但副选还在
                    if(random<mainBase+subUp*subBase){//副选
                        return SUB;
                    }
                }
                return NOT;

            }
            return NULL;
        }



        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Integer getNoHave() {
            return noHave;
        }

        public void setNoHave(Integer noHave) {
            this.noHave = noHave;
        }

        public Integer getMainUp() {
            return mainUp;
        }

        public void setMainUp(Integer mainUp) {
            this.mainUp = mainUp;
        }

        public Integer getSubUp() {
            return subUp;
        }

        public void setSubUp(Integer subUp) {
            this.subUp = subUp;
        }
    }
}
