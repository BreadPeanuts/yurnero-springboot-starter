package com.github.weibo1987.starter.autoconfigure.support;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

/**
 * 接口返回对象框架，具体的数据在data里，用实现ResultData的对象..
 *
 * @author weibo
 * @date: 2018/4/9 14:19
 */
@Data
public class YurneroJsonResult {

    private String code;

    private String msg;

    private Object data;

    /**
     * 封装返回一个指定的对象.
     *
     * @param errorCode int value of return code
     * @param msg       String value of message
     * @return {@link YurneroJsonResult}
     */
    public static YurneroJsonResult build(String errorCode, String msg) {
        return build(errorCode, msg, new EmptyObject());
    }

    /**
     * 构建错误返回结果.
     *
     * @param errorType 错误类型.
     * @return {@link YurneroJsonResult}
     */
    public static YurneroJsonResult build(ErrorType errorType) {
        YurneroJsonResult result = new YurneroJsonResult();
        result.setCode(errorType.getErrorCode());
        result.setMsg(errorType.getErrorMsg());
        result.setData(new EmptyObject());
        return result;
    }

    /**
     * 返回通用对象.
     *
     * @param errorCode  返回码
     * @param msg        返回信息
     * @param resultData 对象
     * @return {@link YurneroJsonResult}
     */
    public static YurneroJsonResult build(String errorCode, String msg, Object resultData) {
        YurneroJsonResult result = new YurneroJsonResult();
        result.setCode(errorCode);
        result.setMsg(msg);
        result.setData(resultData);
        return result;
    }

    /**
     * 空对象返回.
     */
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class EmptyObject {
    }
}
