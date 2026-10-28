package com.demo.foundation.core.exception;

/**
 * 业务异常基类。
 * 携带 int code，默认 CODE_UNKNOWN_ERROR(-1)。
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 7886879810834395424L;

    public static final int CODE_UNKNOWN_ERROR = -1;

    protected int code = CODE_UNKNOWN_ERROR;

    public BizException(int code) {
        this.code = code;
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message, Throwable e) {
        super(message, e);
    }

    public BizException(int code, String message, Throwable e) {
        super(message, e);
        this.code = code;
    }

    public BizException(int code, Throwable e) {
        super(e);
        this.code = code;
    }

    public BizException(Throwable e) {
        super(e);
        this.code = CODE_UNKNOWN_ERROR;
    }

    public BizException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }
}
