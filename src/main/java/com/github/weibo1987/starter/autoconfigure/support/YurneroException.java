package com.github.weibo1987.starter.autoconfigure.support;

import lombok.Data;

/**
 * 类说明.
 *
 * @author weibo
 * @date: 2018/4/9 15:01
 */
@Data
public class YurneroException extends RuntimeException {

    private String errorCode;
    private String errorMsg;
    private Object data;

    public YurneroException(ErrorType exceptionType, Object data) {
        super(exceptionType.getErrorMsg());
        this.setErrorCode(exceptionType.getErrorCode());
        this.setErrorMsg(exceptionType.getErrorMsg());
        this.setData(data);
    }

    public YurneroException(ErrorType exceptionType) {
        this(exceptionType, null);
    }

    public YurneroException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.setErrorCode(errorCode);
        this.setErrorMsg(errorMsg);
    }
}
