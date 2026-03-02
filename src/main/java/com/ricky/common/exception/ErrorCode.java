package com.ricky.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BAD_REQUEST(1000, HttpStatus.BAD_REQUEST, "bad_request"),
    VALIDATION_FAILED(1001, HttpStatus.BAD_REQUEST, "validation_failed"),
    UNAUTHORIZED(1002, HttpStatus.UNAUTHORIZED, "unauthorized"),
    FORBIDDEN(1003, HttpStatus.FORBIDDEN, "forbidden"),
    NOT_FOUND(1004, HttpStatus.NOT_FOUND, "not_found"),
    CONFLICT(1005, HttpStatus.CONFLICT, "conflict"),
    RATE_LIMITED(1006, HttpStatus.TOO_MANY_REQUESTS, "rate_limited"),
    INTERNAL_ERROR(1007, HttpStatus.INTERNAL_SERVER_ERROR, "internal_error"),
    SERVICE_UNAVAILABLE(1008, HttpStatus.SERVICE_UNAVAILABLE, "service_unavailable"),

    USER_NOT_FOUND(2001, HttpStatus.NOT_FOUND, "user_not_found"),
    MODEL_NOT_FOUND(2002, HttpStatus.NOT_FOUND, "model_not_found"),
    EMAIL_EXISTS(2003, HttpStatus.CONFLICT, "email_exists"),
    INVALID_CREDENTIALS(2004, HttpStatus.UNAUTHORIZED, "invalid_credentials"),
    INVALID_REFRESH_TOKEN(2005, HttpStatus.UNAUTHORIZED, "invalid_refresh_token"),
    FILE_TOO_LARGE(2006, HttpStatus.PAYLOAD_TOO_LARGE, "file_too_large"),
    MODEL_DISABLED(2007, HttpStatus.BAD_REQUEST, "model_disabled");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
