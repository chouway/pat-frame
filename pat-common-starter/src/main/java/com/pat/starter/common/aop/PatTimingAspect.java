package com.pat.starter.common.aop;

import org.perf4j.LoggingStopWatch;
import org.perf4j.aop.AbstractTimingAspect;
import org.perf4j.slf4j.Slf4JStopWatch;

public class PatTimingAspect extends AbstractTimingAspect {

    @Override
    protected LoggingStopWatch newStopWatch(String loggerName, String levelName) {
        return new Slf4JStopWatch();
    }
}
