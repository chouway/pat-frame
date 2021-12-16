package com.pat.starter.cache.aop;

import com.pat.starter.cache.annotation.LzxLockDistributed;
import com.pat.starter.common.util.PatTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.integration.redis.util.RedisLockRegistry;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * MethodLockAop
 *
 * @author zhouyw
 * @date 2021.03.20
 */
@Aspect
@Slf4j
public class MethodLockAop {


    private RedisLockRegistry redisLockRegistry;

    public MethodLockAop(RedisLockRegistry redisLockRegistry) {
        this.redisLockRegistry = redisLockRegistry;
    }

    @Pointcut("@annotation(com.pat.starter.cache.annotation.LzxLockDistributed)")
    private void apiAop() {

    }

    /**
     * 默认的key  类.方法名
     */
    private String DEF_LOCK_KEY = "%s.%s";
    /**
     * 当前的key  类.方法名：参数值
     */
    private String CUR_LOCK_KEY = "%s.%s:%s";

    @PostConstruct
    public void init() {
        log.info("method lock aop init");
    }

    @Around("apiAop()")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        LzxLockDistributed lzxLockDistributed = method.getAnnotation(LzxLockDistributed.class);


        String lockKey = this.getLockKey(point,lzxLockDistributed);
        String lockKeyHash = String.format("LOCK_%s",Integer.toString(lockKey.hashCode()));
        Lock lock = this.redisLockRegistry.obtain(lockKeyHash);


        boolean tryLock = lock.tryLock(lzxLockDistributed.time(), TimeUnit.SECONDS);;
        log.debug("lockKey={},hash={},tryLock={}",lockKey,lockKeyHash,tryLock);
        if(!tryLock){
            throw new RuntimeException(String.format("lock[%s] busy",lockKey));
        }
        Object proceed = null;
        try {
            proceed = point.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                lock.unlock();
                log.debug("lockKey={},hash={},unlock",lockKey,lockKeyHash);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }


        return proceed;

    }

    /**
     * 获取转译后的 lockKey
     * <p>
     * 动态模版
     *
     * @param lzxAKey 注解上的key
     * @param method  方法
     * @param methodSignature 方法特征
     * @param args  运行时的方法参数
     * @return
     */
    private String getLockKey(ProceedingJoinPoint point,  LzxLockDistributed lzxLockDistributed) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        String lzxAKey = lzxLockDistributed.value();


        Class declaringType = methodSignature.getDeclaringType();
        String serviceName = declaringType.getSimpleName();
        String methodName = method.getName();

        Object[] args = point.getArgs();

        if (args==null||args.length<1||!PatTemplateUtil.isExistDynamicParam(lzxAKey)) {//不存在动态变量地直接返回
            return String.format(DEF_LOCK_KEY, serviceName, methodName);
        }

        String[] parameterNames = methodSignature.getParameterNames();

        Map<String, Object> content = new HashMap<String, Object>();
        for (int i = 0; i < args.length; i++) {
            content.put(parameterNames[i],args[i]);
        }
        String lzxAVal = PatTemplateUtil.transform(lzxAKey,content);
        return String.format(CUR_LOCK_KEY, serviceName, methodName,lzxAVal);
    }
}
