package com.pat.starter.common.aop;

import com.pat.api.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * PatServiceAop
 *
 * 切面会自动排除掉对依赖的切面
 * 依赖控制反转体现
 *
 * @author zhouyw
 * @date 2021.03.20
 */
@Aspect
@Slf4j
public class PatServiceAop implements MethodInterceptor {

    @Autowired
    private com.pat.starter.common.util.PatErrorUtil aibkErrorUtil;

    @Autowired
    private com.pat.starter.common.config.PatCommonData aibkCommonData;

    @PostConstruct
    public void init() {
        log.info("service aop init : {}",aibkCommonData.getAopService());
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object proceed = null;
        try {
            proceed = invocation.proceed();
        } catch (BusinessException e) {
            aibkErrorUtil.logEror(invocation, e);
            throw e;
        } catch (Exception e){
            aibkErrorUtil.logEror(invocation, e);
            throw new BusinessException(e.getMessage());
        }
        return proceed;

    }


}
