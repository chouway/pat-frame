package com.pat.starter.common.simple;

import cn.hutool.core.net.NetUtil;
import com.pat.api.entity.PatError;
import com.pat.api.entity.ext.PatErrorExt;
import com.pat.api.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExceptionTest
 *
 * @author chouway
 * @date 2021.05.14
 */
@Slf4j
public class ExceptionTest {
    @Test
    public void simple() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "world");
        String msg = null;
        PatErrorExt aibkErrorExt = new PatErrorExt();
        aibkErrorExt.setMessage("abc");

        context.put("aibkError", aibkErrorExt);
        String transform = com.pat.starter.common.util.PatTemplateUtil.transform(com.pat.starter.common.config.PatCommonData.ERROR_TEMPLATE_FILE, context);
        log.info("simple-->transform={}", transform);

    }


    @Test
    public void simple2() {
        try {
            if (true) {
                throw new BusinessException("abc");
            }
        } catch (Exception e) {
            PatErrorExt aibkError = new PatErrorExt();
            aibkError.setException(e.getClass().getCanonicalName());
            aibkError.setMessage(e.getMessage());
            StackTraceElement[] stackTrace = e.getStackTrace();
            String stackTraceKey = "com.aibk";
            List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if (stackTraceElement.getClassName().contains(stackTraceKey)) {
                    stackTraceElements.add(stackTraceElement);
                }
            }
            aibkError.setStackTraceElements(stackTraceElements);

            Map<String, Object> context = new HashMap<String, Object>();
            context.put("aibkError",aibkError);
            String transform = com.pat.starter.common.util.PatTemplateUtil.transform(com.pat.starter.common.config.PatCommonData.ERROR_TEMPLATE_FILE, context);
            log.info("simple2-->transform={}", transform);
            log.error("error:-->{}", e.getMessage(), e);
        }
    }

    @Test
    public void getIp(){
        String localhostStr = NetUtil.getLocalhostStr();
        log.info("getIp-->localhostStr={}", localhostStr);

    }
}
