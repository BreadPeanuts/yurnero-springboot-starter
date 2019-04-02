package com.github.weibo1987.starter.autoconfigure.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 错误码描述.
 *
 * @author : weibo
 * @version : v1.0
 * @since : 2019/3/14 11:04
 */
@Data
@ConfigurationProperties(prefix = "yurnero.error-meta")
public class YurneroErrorCodeProperties {
    private String successCode;
    private String undefinedErrorCode;
    private String undefinedErrorMsg;
    private String paramValidateErrorCode;
    private String paramValidateErrorMsg;
    private List<String> traceExcludeUrls;
}
