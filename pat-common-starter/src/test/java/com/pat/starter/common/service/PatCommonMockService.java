package com.pat.starter.common.service;

import com.pat.starter.common.anno.PatAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.perf4j.aop.Profiled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * AibkCommonMockService
 *
 * @author chouway
 * @date 2021.05.13
 */
@Slf4j
@Service
public class PatCommonMockService {

    @Profiled(tag = "commonMock.mock({$0})")
    @PatAnnotation(key="#msg")
    public String mock(String msg){
        log.info("mock-->msg={}", msg);
        if(true){
            throw new RuntimeException("test异常");
        }
        
        if(!StringUtils.hasText(msg)){
            return "hello";
        }
        return  "hello,"+msg;
    }

    public String mockParamEmpty(){
        return null;
    };


}
