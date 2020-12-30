package com.kyle.demo.result;

/**
 * @author kz37
 */
public enum ResultCode {
    // success
    SUCCESS(200),
    // data format error
    FAIL(500),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    // server error
    INTERNAL_SERVER_ERROR(500),

    SERVER_BUSY(503);

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

}
