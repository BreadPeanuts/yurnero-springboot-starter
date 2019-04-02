package com.github.weibo1987.starter.autoconfigure.filter;

import com.github.weibo1987.starter.autoconfigure.property.YurneroErrorCodeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 类说明.
 *
 * @author weibo
 * @since 2018/8/13
 */
@Configuration
public class FilterConfig {

    private Integer order = Ordered.HIGHEST_PRECEDENCE + 100;


    @Autowired
    private YurneroErrorCodeProperties yurneroErrorCodeProperties;

    /**
     * 可读httprequest filter.
     *
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<HttpServletRequestReplacedFilter>
        httpServletRequestReplacedFilterFilterRegistrationBean() {
        FilterRegistrationBean<HttpServletRequestReplacedFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new HttpServletRequestReplacedFilter());
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.setOrder(order + 2);
        return filterRegBean;
    }

    /**
     * trace日志filter.
     *
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<TraceLoggingFilter> traceLoggingFilterFilterRegistrationBean() {
        FilterRegistrationBean<TraceLoggingFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new TraceLoggingFilter(yurneroErrorCodeProperties));
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.setOrder(order + 1);
        return filterRegBean;
    }

    /**
     * xss 防御 filter.
     *
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<XssAntiFilter> xssAntiFilterFilterRegistrationBean() {
        FilterRegistrationBean<XssAntiFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new XssAntiFilter());
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.setOrder(order);
        return filterRegBean;
    }
}
