package com.pat.starter.common.util;

import cn.hutool.core.util.IdUtil;
import org.springframework.util.StringUtils;

/**
 * AibkLogIdUtils
 *
 * @author chouway
 * @date 2021.05.16
 */
public class PatLogIdUtils {

    private static final ThreadLocal<String> logIdCache
            = new ThreadLocal<String>();

    public static String getLogId() {
        return logIdCache.get();
    }

    public static String generateLogId(){
        String logId = IdUtil.simpleUUID();
        setLogId(logId);
        return logId;
    }

    public static void setLogId(String traceId) {
        logIdCache.set(traceId);
    }

    public static void clear() {
        logIdCache.remove();
    }

}
