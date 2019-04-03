package com.github.weibo1987.starter.autoconfigure.utils;

import org.slf4j.MDC;

/**
 * 类说明.
 *
 * @author weibo
 * @since: 2018/4/10 10:53
 */
public class TraceIdUtils {

    /**
     * traceId.
     */
    public static final String TRACE_ID = "TRACE_ID";

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static void remove() {
        MDC.remove(TRACE_ID);
    }
}
