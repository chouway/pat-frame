package com.pat.starter.common.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pat.api.constant.PatConstant;
import com.pat.api.entity.PatError;
import com.pat.api.entity.ext.PatErrorExt;
import com.pat.api.exception.BusinessException;
import com.pat.api.service.error.IPatErrorService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.*;

/**
 * PatErrorUtil
 *
 * @author chouway
 * @date 2021.05.15
 */
@Slf4j
public class PatErrorUtil {

    private ThreadLocal<Long> errorIdCache = new ThreadLocal<Long>();

    @Autowired(required = false)
    private IPatErrorService aibkErrorService;

    @Autowired
    private com.pat.starter.common.config.PatCommonData aibkCommonData;

    @Autowired
    private Environment env;

    private static ParserConfig PARSER_CONFIG = new ParserConfig();

    static {
        PARSER_CONFIG.setAutoTypeSupport(true);
    }


    /**
     * log 异常信息
     *
     * @param invocation
     * @param e
     */
    public void logEror(MethodInvocation invocation, Exception e) {
        try {
            PatErrorExt aibkError = initPatError(invocation, e);
            if (aibkErrorService == null) {
                log.error("error:-->aibkError={}", JSON.toJSONString(aibkError), e);
                return;
            }
            final String logId = this.getLogId();
            log.error("error:-->logId={},aibkError={}", logId, JSON.toJSONString(aibkError), e);
            String stackTrace = getStackTrace(aibkError, e);
            aibkError.setProject(env.getProperty("spring.application.name"));
            aibkError.setStackTrace(stackTrace);
            aibkError.setLogId(logId);
            aibkError.setIp(NetUtil.getLocalhostStr());
            Long handlErrorId = errorIdCache.get();
            aibkError.setHandleErrorId(handlErrorId);
            if (aibkCommonData.getErrorSaveAsynch()) {
                ThreadUtil.execAsync(new Runnable() {
                    @Override
                    public void run() {
                        Long errorId = aibkErrorService.add(aibkError);
                        log.error("error:-->logId={},errorId={}", logId,errorId);
                    }
                });
            } else {
                Long errorId = aibkErrorService.add(aibkError);
                log.error("error:-->logId={},errorId={}", logId,errorId);
            }
        } catch (Exception ee) {
            throw new BusinessException("logError失败", ee);
        }
    }

    private String getLogId() {
        String logIdTemp = PatLogIdUtils.getLogId();
        if(ObjectUtils.isEmpty(logIdTemp)){
            logIdTemp = PatLogIdUtils.generateLogId();
        }
        return logIdTemp;
    }

    public static void retry(Long errorId){
        SpringUtil.getBean(PatErrorUtil.class).handleError(errorId);
    }
    /**
     * 重试异常
     */
    public void handleError(Long errorId) {
        try {
            errorIdCache.set(errorId);
            log.info("handlError-->errorId={}", errorId);
            if (aibkErrorService == null) {
                throw new BusinessException("aibkErrorService服务不存在");
            }
            PatError aibkError = aibkErrorService.getById(errorId);
            if (aibkError == null) {
                log.info("handlError-->aibkError=null");
                return;
            }
            if (PatConstant.SUCCESS.equals(aibkError.getStatus())) {
                log.info("handlError-->aibkError=已成功，不需要重试");
                return;
            }
            String service = aibkError.getService();
            Object serviceBean = SpringUtil.getBean(service);
            String paramClazz = aibkError.getParamClazz();
            Method method = null;
            if (ObjectUtils.isEmpty(paramClazz)) {
                method = serviceBean.getClass().getMethod(aibkError.getMethod());
            } else {
                JSONArray paramClazzArray = JSON.parseArray(paramClazz);
                Class[] paramClazzes = new Class[paramClazzArray.size()];
                for (int i = 0; i < paramClazzArray.size(); i++) {
                    paramClazzes[i] = Class.forName(paramClazzArray.get(i).toString());
                }
                method = serviceBean.getClass().getMethod(aibkError.getMethod(), paramClazzes);
            }
            String paramJson = aibkError.getParamJson();
            Object result = null;
            if (ObjectUtils.isEmpty(paramJson)) {
                result = ReflectionUtils.invokeMethod(method, serviceBean);
            } else {
                Object[] params = JSON.parseObject(paramJson, Object[].class, PARSER_CONFIG);
                result = ReflectionUtils.invokeMethod(method, serviceBean, params);
            }
            this.handleResult(errorId, null, result);
        } catch (BusinessException e) {
            log.error("busi error:{}-->[errorId]={}", e.getMessage(), JSON.toJSONString(new Object[]{errorId}), e);
            this.handleResult(errorId, e, null);
            throw e;
        } catch (Exception e) {
            log.error("error:{}-->[errorId]={}", e.getMessage(), JSON.toJSONString(new Object[]{errorId}), e);
            this.handleResult(errorId, e, null);
            throw new BusinessException("handleError失败", e);
        } finally {
            errorIdCache.set(null);
        }

    }

    private void handleResult(Long errorId, Exception e, Object handleResult) {
        PatError aibkError = new PatError();
        aibkError.setId(errorId);
        if (e == null) {
            aibkError.setStatus(PatConstant.SUCCESS);
            aibkError.setHandleResultJson(JSON.toJSONString(handleResult, SerializerFeature.WriteClassName));
        } else {
            aibkError.setStatus(PatConstant.FAIL);
            aibkError.setRemark(e.getMessage());
        }
        log.info("handleResult-->aibkError={}", JSON.toJSONString(aibkError));
        if (aibkErrorService != null) {
            aibkErrorService.handle(aibkError);
        }
    }


    private PatErrorExt initPatError(MethodInvocation invocation, Exception e) {
        PatErrorExt aibkError = new PatErrorExt();
        aibkError.setException(e.getClass().getCanonicalName());
        aibkError.setMessage(e.getMessage());
        if (e instanceof BusinessException) {
            BusinessException busiError = (BusinessException) e;
            aibkError.setErrorCode(busiError.getCode());
        }


        AccessibleObject staticPart = invocation.getStaticPart();

        Method method = invocation.getMethod();
        aibkError.setService(StringUtils.uncapitalize(method.getDeclaringClass().getSimpleName()));
        aibkError.setMethod(method.getName());
        Object[] arguments = invocation.getArguments();
        Class[] paramClasses = method.getParameterTypes();
        if (paramClasses != null) {
            List<String> paramClazzList = new ArrayList<String>();
            for (Class paramClazz : paramClasses) {
                paramClazzList.add(paramClazz.getCanonicalName());
            }
            aibkError.setParamClazz(JSON.toJSONString(paramClazzList));
        }
        if (arguments != null) {
            aibkError.setParamJson(JSON.toJSONString(arguments, SerializerFeature.WriteClassName));
        }
        return aibkError;
    }

    private String getStackTrace(PatErrorExt aibkError, Exception e) {
        String stackTraceKey = aibkCommonData.getStackTraceKey();
        StackTraceElement[] stackTraceElementArray = e.getStackTrace();
        List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>();
        if (ObjectUtils.isEmpty(stackTraceKey)) {
            stackTraceElements = Arrays.asList(stackTraceElementArray);
        } else {
            stackTraceElements = new ArrayList<StackTraceElement>();
            for (StackTraceElement stackTraceElement : stackTraceElementArray) {
                if (stackTraceElement.getLineNumber() != -1 && stackTraceElement.getClassName().indexOf(stackTraceKey) == 0) {
                    stackTraceElements.add(stackTraceElement);
                }
            }
        }
        aibkError.setStackTraceElements(stackTraceElements);
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("aibkError", aibkError);
        return PatTemplateUtil.transform(com.pat.starter.common.config.PatCommonData.ERROR_TEMPLATE_FILE, context);
    }
}
