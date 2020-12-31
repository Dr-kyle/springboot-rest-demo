package com.kyle.demo.exception;

/**
 * @author kz37
 */
public class OauthException extends RuntimeException {
    public OauthException(String msg) {
        super(msg);
    }

    public OauthException() {
        super();
    }

    public OauthException(String message, Throwable cause) {
        super(message, cause);
    }
}
