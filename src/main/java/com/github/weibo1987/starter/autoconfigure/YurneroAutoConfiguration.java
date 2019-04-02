package com.github.weibo1987.starter.autoconfigure;

import com.github.weibo1987.starter.autoconfigure.filter.FilterConfig;
import com.github.weibo1987.starter.autoconfigure.property.YurneroErrorCodeProperties;
import com.github.weibo1987.starter.autoconfigure.support.ErrorHandler;
import com.github.weibo1987.starter.autoconfigure.support.YurneroResponseBodyAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Yurnero自动配置类.
 *
 * @author : weibo
 * @version : v1.0
 * @since : 2019/3/14 10:23
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties( {YurneroErrorCodeProperties.class})
@Import(value = {ErrorHandler.class, YurneroResponseBodyAdvice.class, FilterConfig.class})
public class YurneroAutoConfiguration {


}
