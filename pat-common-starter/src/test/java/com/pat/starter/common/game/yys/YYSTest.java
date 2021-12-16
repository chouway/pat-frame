package com.pat.starter.common.game.yys;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

/**
 * YYSTest
 *
 * @author chouway
 * @date 2021.07.20
 */
@Slf4j
public class YYSTest {

    //运算公式
    private String expressionMeta = "";
    //运算参数
    private Map<String, Object> params = new HashMap<String, Object>();
    //结果描述
    private String descMeta = "";

    @Test
    public void simple() {
        this.expressionMeta = "Math.pow(2,3)";
        this.descMeta = "${result}";
        this.calcResult();

        Integer cmn = Cmn(2, 10);
        log.info("simple-->cmn={}", cmn);

        Integer px = nx(4);
        log.info("simple-->px={}", px);

        Integer pmn = Pmn(2,4);
        log.info("simple-->pmn={}", pmn);

        //白出现概率0.1  黑0.9 白白 白黑 黑白 黑黑 0.1*0.1 ， 0.1*0.9 ， 0.9*0.1 ，0.9*0.9
        this.expressionMeta = "0.1*0.1+0.1*0.9+0.9*0.1+0.9*0.9";
        this.descMeta = "白出现概率0.1  黑0.9 白白 白黑 黑白 黑黑总概率:${result}";
        this.calcResult();
    }

    /**
     * sp稻荷神速度与反超计算
     *
     * @throws ScriptException
     */
    @Test
    public void calcByJSA() throws ScriptException {
        this.expressionMeta = "(${targetSD}-${basicSD})/((${basicSD}+30)/${targetSD}-1)+${targetSD}";
        //sp麓 115+13.867  sp花108+21.6118 千姬 241
        //sp稻荷神御馔津:速度111.84满技能,在不小于277.457的一速跑道下才能会比速度128的友方单位先行动;(阴阳师)
        //sp稻荷神御馔津:速度111.84满技能,在不小于298.004的一速跑道下才能会比速度128.867的友方单位先行动;(sp麓)
        //sp稻荷神御馔津:速度111.84满技能,在不小于317.983的一速跑道下才能会比速度129.6118的友方单位先行动;(sp花)
        this.params.put("targetSD", ""+(108+21.6118));
        this.params.put("basicSD", ""+(109+2.84));
        this.descMeta = "sp稻荷神御馔津:速度${basicSD}满技能,在不小于${result}的一速跑道下才能会比速度${targetSD}的友方单位先行动;";
        this.calcResult();
    }

    /**
     * 一速拉条推条计算
     *
     * @throws ScriptException
     */
    @Test
    public void calcByJSB() throws ScriptException {

        this.params.put("highSpeed", 271);
        this.expressionMeta = "${highSpeed}*0.7";
        this.descMeta = "一速${highSpeed}拉条,二速${result}才不会被反超";
        Object secodeSpeed = this.calcResult();
        this.params.put("secodeSpeed", secodeSpeed);

        this.expressionMeta = "${highSpeed}/1.4*0.7";
        this.descMeta = "一速${highSpeed}拉条,二速鬼吞(至少${secodeSpeed})推条，输出单位需要${result}才不会被反超";
        this.calcResult();

        this.expressionMeta = "${highSpeed}*0.4";
        this.descMeta = "一速${highSpeed}拉条,二速(至少${secodeSpeed})拉条，输出单位需要${result}才不会被反超";
        this.calcResult();

        this.expressionMeta = "${highSpeed}*0.9";
        this.descMeta = "一速${highSpeed}，千姬速度不小于${result}在一速行动后会接着行动";
        this.calcResult();

        this.expressionMeta = "${highSpeed}*0.8";
        this.descMeta = "一速${highSpeed}，千姬速度不小于${result}在对面二次行动后会接着行动";
        this.calcResult();


    }

    @Test
    public void calcByJSC() throws ScriptException {
        this.expressionMeta = "${life}*0.1*2.2069";
        this.params.put("life", Float.toString(11393f + 18185.4884f));
        this.descMeta = "地藏会生成盾:${result}";//6,527.677
        this.calcResult();

    }

    /**
     * 默认概率: SP  0.25%;   SSR 1%;  活动有2.5倍加成
     * <p>
     * 全图鉴证加成     从 10% （0抽）  20%（50）  100% （500抽）  600抽保底出
     * 无全图加成      从  0% （0抽）             15%  （500抽）
     * <p>
     * 精确对照表：我攒了XXX抽，有多大概率抽出当期式神？
     * https://bbs.nga.cn/read.php?tid=20722748
     * <p>
     * <p>
     * 使用掉三次up的期望是:96
     * 3*1/(0.0125*2.5)=96 抽。
     */
    @Test
    public void calcByJSD() {
        this.expressionMeta = "1/(0.0125)";
        this.descMeta = "无up抽出的期望是:${result}";//80
        this.calcResult();

        this.expressionMeta = "1/(0.0125*2.5)";
        this.descMeta = "使用掉1次up的期望是:${result}";//32
        this.calcResult();

        //求多少抽数刚好用完3次up，此时获得当期式神的概率为多少
        this.expressionMeta = "3*1/(0.0125*2.5)";
        this.descMeta = "使用掉三次up的期望是:${result}";//96
        this.calcResult();

        this.expressionMeta = "Math.pow(1-0.0125*2.5,50)";
        this.descMeta = "50抽抽不出up是概率:${result}";//0.204
        Object p50N = this.calcResult();

        this.params.put("p50N", p50N);
        this.expressionMeta = "(1-${p50N})";
        this.descMeta = "50抽有抽出up的概率:${result}";//0.796
        Object p50Y = this.calcResult();
        this.params.put("p50Y", p50Y);

        int n = 30;//抽数
        this.params.put("n",n);

        //抽不中概率0.96875  up概率 0.03125
        this.expressionMeta = "1-Math.pow(0.96875,${n})";
        this.descMeta = "${n}抽内用掉0次up的概率:${result}";
        this.calcResult();

        this.expressionMeta = "Math.pow(0.96875,${n}-1)*0.03125";
        this.descMeta = "${n}抽内用掉1次up的概率:${result}";
        this.calcResult();


//        this.expressionMeta = "Math.pow(0.0125*2.5,)";//50抽内用完up,抽到当期式神的概率   C 3 50  50*49*48/3*2
//        this.descMeta = "50抽内用完up,抽到当期式神的概率率:${result}";
//        Object p100 = this.calcResult();


    }

    /**
     * 久次良加爆伤30%  兔子加20%
     */
    @Test
    public void calcByJSE() {
        this.params.put("basicAX","4127");
        this.params.put("basicAY","5236");
        this.params.put("basicB","2.79");
        this.expressionMeta = "(${basicAX}+${basicAY})*${basicB}";
        this.descMeta = "式神无加成默认输出伤害:${result}";
        this.calcResult();

        this.expressionMeta = "(${basicAX}+${basicAY})*(2.79+0.3)";
        this.descMeta = "久次良加成后伤害:${result}";
        this.calcResult();

        this.expressionMeta = "(${basicAX}*1.2+${basicAY})*2.79";
        this.descMeta = "兔子加成后伤害:${result}";
        this.calcResult();
    }

    /**
     * 一个黑箱子里有  10个球  1个白的 9个黑的。抽出并且放回。
     *
     *  问10抽出现1次白的概率是多少？   x/100 = p(白) = 0.1  x = 10
     */
    @Test
    public void abc(){
        //  C10f100*0.1^10*0.9^90
        //命中p次数
        int m = 4;
        //抽次数
        int n = 10;  //白白 白黑 黑白 黑黑 0.1*0.1 ， 0.1*0.9 ， 0.9*0.1 ，0.9*0.9
        //命中p 概率
        float p = 0.1f;
        //命中非p 概率
        float np = 1 - p;
        Integer cmn = this.Cmn(m, n);
        this.params.put("m",m+"");
        this.params.put("n",n+"");
        this.params.put("p",p+"");
        this.params.put("np",np+"");
        this.params.put("cmn",""+cmn);
        this.expressionMeta = "${cmn}*Math.pow(${p},${m})*Math.pow(${np},${n}-${m})";
        this.descMeta = "白概率${p},黑概率${np}，重复抽${n}次，出现${m}次白的概率:${result}";
        this.calcResult();
    }

    /**
     * 0   0.042
     * 1   0.135
     * 2   0.215
     * 3   0.227
     * 4   0.177
     * 4+   0.204
     */
    @Test
    public void abc2(){
        //命中p 概率
        float p = 0.0125f*2.5f;
        //命中非p 概率
        float np = 1 - p;
        this.params.put("p",p+"");
        this.params.put("np",np+"");

        //  C10f100*0.1^10*0.9^90
        //至少命中p次数
        int m = 5; //最少为1

        //抽次数
        int n = 100;
        this.params.put("n",n);
        this.params.put("m",m);
        if(m<1){
            this.expressionMeta="1";
            this.descMeta = "白概率${p},黑概率${np}，重复抽${n}次，至少出现${m}次白的概率:${result}";
            this.calcResult();
            return;
        }
        Float temp = 0f;
        for (int i = 0; i < m; i++) {
             temp += this.calcTempAbc(i, n);
        }
        this.expressionMeta="1-${temp}";
        this.params.put("temp",temp);
        this.params.put("m",m);
        this.descMeta = "白概率${p},黑概率${np}，重复抽${n}次，至少出现${m}次白的概率:${result}";
        this.calcResult();
    }

    private Float calcTempAbc(int m ,int n){
        this.params.put("m",m+"");
        Integer cmn = this.Cmn(m, n);
        this.params.put("cmn",""+cmn);
        this.expressionMeta = "${cmn}*Math.pow(${p},${m})*Math.pow(${np},${n}-${m})";
        this.descMeta = "白概率${p},黑概率${np}，重复抽${n}次，出现${m}次白的概率:${result}";
        Object result = this.calcResult();
        return Float.parseFloat(result.toString());
    }



    private Object calcResult() {
        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
        String expression = com.pat.starter.common.util.PatTemplateUtil.transform(this.expressionMeta, params);
        log.info("calcByJS-->expression={}", expression);
        Object result = null;
        try {
            result = jse.eval(expression);
        } catch (ScriptException e) {
            throw new RuntimeException("计算失败", e);
        }
        log.info("calcByJS-->result={}", result);
        this.params.put("result", result);
        String desc = com.pat.starter.common.util.PatTemplateUtil.transform(this.descMeta, params);
        log.info("calcByJS-->desc={}", desc);
        return result;
    }

    /***
     * 组合运算
     *
     * 咱们聊的第二个概念是“组合”，它比排列更常用，组合的英文是 Combination，因此在数学符号中用 C 表示，美国和英国教材中，也常用“长括号”表示组合数。我们常见的 C 右边会跟两个数字（或字母），右下角的数字 n 表示总数，右上角的数字 m 表示抽出的个数。整个符号的意思是“从 n 个人中，不计顺序地抽出 m 个人的抽法数”，可以读作“C n 抽 m”。
     *
     * 作者：浣熊数学
     * 链接：https://www.zhihu.com/question/26094736/answer/610713978
     * 来源：知乎
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     *
     * @param m
     * @param n
     * @return
     */
    private Integer Cmn(int m, int n) {
        if (m < 0 || n < 0) {
            throw new RuntimeException("阶乘数值不能小于0");
        }
        if (m > n) {
            throw new RuntimeException("组合m不能大于n");
        }

        if (m == 0 || m == n) {
            return 1;
        }

        int upV = 1;
        for (int i = n; i > n - m; i--) {
            upV *= i;
        }
        int downV = 1;
        for (int i = 2; i < m + 1; i++) {
            downV *= i;
        }
        return upV / downV;
    }

    /**
     * 排列组合
     * 所谓排列组合，排列在组合之前，咱们要聊的第一个概念是“排列”，排列的英文是 Permutation 或者 Arrangement，因此在数学符号中，用 P 或者 A 表示都可以，二者意思完全一样。我们常见的 P 右边会跟两个数字（或字母），右下角的数字 n 表示总数，右上角的数字 m 表示抽出的个数。整个符号的意思是“从 n 个人中，有顺序地抽出 m 个人的抽法数”，可以读作“P n 抽 m”。
     *
     * 作者：浣熊数学
     * 链接：https://www.zhihu.com/question/26094736/answer/610713978
     * 来源：知乎
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param m
     * @param n
     * @return
     */
    private Integer Pmn(int m, int n) {
        if (m < 0 || n < 0) {
            throw new RuntimeException("阶乘数值不能小于0");
        }
        if (m > n) {
            throw new RuntimeException("排列m不能大于n");
        }

        if (m == 0 || m == n) {
            return 1;
        }

        int upV = 1;
        for (int i = n; i > n - m; i--) {
            upV *= i;
        }
        return upV;
    }
    /**
     * 阶乘
     * @param x
     * @return
     */
    private Integer nx(int x) {
        if (x < 0) {
            throw new RuntimeException("阶乘数值不能小于0");
        }

        if (x == 0) {
            return 1;
        }

        int val = 1;
        for (int i = x; i > 1; i--) {
            val *= i;
        }
        return val;
    }


    /**
     * y 代表 高速铁鼠   x代表 慢速的强输出
     * 158为魂土的最高速度
     *
     * (y-x) /x   =   （y - 158） /(158*0.8)
     */
    @Test
    public void tishutuzhiqu(){
        this.params.put("x","146");
        this.expressionMeta = "0.25/(1/126.4 - 1/${x})";
        this.descMeta = "最强输出的速度 ${x},需要铁鼠土蜘蛛的速度:${result}，才能减速二层怪达到超速";
        this.calcResult();
    }
}
