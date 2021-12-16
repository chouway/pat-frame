package com.pat.starter.common.simple;

import com.pat.starter.common.service.PatCommonMockService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ParameterizedTypeTest
 *
 * @author chouway
 * @date 2021.05.14
 */
@Slf4j
public class ParameterizedTypeTest {

    @Test
    public void simple() throws NoSuchMethodException {
        Method[] methods = PatCommonMockService.class.getDeclaredMethods();
        if (methods != null) {
            for (Method method : methods) {
                log.info("simple-->method={}", method);

                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes != null) {
                    for (Class<?> parameterType : parameterTypes) {
                        log.info("simple-->parameterType={}", parameterType.getCanonicalName());
                    }
                }

                Type[] genericParameterTypes = method.getGenericParameterTypes();
                if (genericParameterTypes != null) {
                    for (Type genericParameterType : genericParameterTypes) {
                        log.info("simple-->genericParameterType={}", genericParameterType.getTypeName());
                    }
                }
            }
        }
    }




}
