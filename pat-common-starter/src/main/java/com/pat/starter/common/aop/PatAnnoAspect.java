package com.pat.starter.common.aop;

import com.pat.starter.common.anno.PatAnnotation;
import com.pat.starter.common.expression.ExpressionEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;

/**
 * PatAnoAspect
 *
 * @author chouway
 * @date 2021.12.31
 */
@Slf4j
@Aspect
public class PatAnnoAspect {

    private ExpressionEvaluator<String> evaluator = new ExpressionEvaluator<>();

    @Before("execution(* *.*(..)) && @annotation(patAnnotation)")
    public void checkEntity(JoinPoint joinPoint, PatAnnotation patAnnotation) {
        String result = getValue(joinPoint, patAnnotation.key());
        log.info("result: " + result);
        System.out.println("running entity check: " + joinPoint.getSignature().getName());
    }

    private String getValue(JoinPoint joinPoint, String condition) {
        return getValue(joinPoint.getTarget(), joinPoint.getArgs(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), condition);
    }

    private String getValue(Object object, Object[] args, Class clazz, Method method,
                          String condition) {
        if (args == null) {
            return null;
        }
        EvaluationContext evaluationContext =
                evaluator.createEvaluationContext(object, clazz, method, args);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        return evaluator.condition(condition, methodKey, evaluationContext, String.class);
    }
}
