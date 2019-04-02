package com.github.weibo1987.starter.autoconfigure.support;

import com.github.weibo1987.starter.autoconfigure.property.YurneroErrorCodeProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashSet;
import java.util.Set;

/**
 * rest统一返回出口.
 *
 * @author weibo
 * @date: 2018/4/9 14:41
 */
@RestControllerAdvice
public class YurneroResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Set<String> EXCLUDE_URL = new HashSet<>(3);

    @Autowired
    private YurneroErrorCodeProperties yurneroErrorCodeProperties;


    static {
        EXCLUDE_URL.add("/swagger-resources/configuration/ui");
        EXCLUDE_URL.add("/swagger-resources");
        EXCLUDE_URL.add("/v2/api-docs");
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType)
            && !YurneroJsonResult.class.isAssignableFrom(returnType.getParameterType());
    }


    @Override
    public Object beforeBodyWrite(Object resultData,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> converterType,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if (null == resultData) {
            return YurneroJsonResult.build(yurneroErrorCodeProperties.getSuccessCode(), "success");
        }

        if (!shouldBuildResult(serverHttpRequest)) {
            return resultData;
        }

        if (resultData instanceof MappingJacksonValue) {
            MappingJacksonValue result = (MappingJacksonValue) resultData;
            Object body = result.getValue();
            result.setValue(
                body == null
                    ? YurneroJsonResult.build(yurneroErrorCodeProperties.getSuccessCode(), "success")
                    : YurneroJsonResult.build(yurneroErrorCodeProperties.getSuccessCode(), "success", body)
            );
            return result;
        }
        return YurneroJsonResult.build(yurneroErrorCodeProperties.getSuccessCode(), "success", resultData);
    }

    private boolean shouldBuildResult(ServerHttpRequest request) {
        String url = request.getURI().getPath();
        return StringUtils.isNotBlank(url)
            && EXCLUDE_URL.stream().noneMatch(excludeUrl -> url.contains(excludeUrl));
    }
}
