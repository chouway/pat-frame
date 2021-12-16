package com.pat.starter.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

/**
 * SimpleUtilTest
 *
 * @author chouway
 * @date 2021.06.30
 */
@Slf4j
public class SimpleUtilTest {

    @Test
    public void calcByJS() throws ScriptException {
        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
        String expressionMeta = "(${targetSD}-${basicSD})/((${basicSD}+30)/${targetSD}-1)+${targetSD}";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("targetSD",128);
        params.put("basicSD",112);
        String  expression = PatTemplateUtil.transform(expressionMeta, params);
        log.info("calcByJS-->expression={}", expression);
        Object result = jse.eval(expression);
        log.info("calcByJS-->result={}", result);
        params.put("result",result);
        String desc = "sp稻菏神御馔津: 速度 ${basicSD}满技能,在${result}的一速下才能超过 速度${targetSD}的式神或阴阳师;";
        log.info("calcByJS-->desc={}", PatTemplateUtil.transform(desc,params));
    }
}
