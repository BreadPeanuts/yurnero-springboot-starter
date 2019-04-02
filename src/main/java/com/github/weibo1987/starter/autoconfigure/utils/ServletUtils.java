package com.github.weibo1987.starter.autoconfigure.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Servlet相关工具,如获取远程ip等.
 *
 * @author weibo
 */
public class ServletUtils {

    /**
     * 从nginx里获取转换过后的用户ip.
     *
     * @param request {@link HttpServletRequest}
     * @return String of ip address
     */
    public static String getRemoteAddressHost(HttpServletRequest request) {
        String host = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(host) || "unknown".equalsIgnoreCase(host)) {
            host = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isBlank(host) || "unknown".equalsIgnoreCase(host)) {
            host = request.getHeader("ip");//这个是客户端传的
        }
        if (StringUtils.isBlank(host) || "unknown".equalsIgnoreCase(host)) {
            host = request.getRemoteAddr();
        }
        if (StringUtils.isNotBlank(host) && host.contains(",")) {
            //取最后一个IP
            //host = host.substring(host.lastIndexOf(",") + 1, host.length()).trim();
            //取第一个IP
            host = host.substring(0, host.indexOf(",")).trim();
        }
        return host;
    }
}
