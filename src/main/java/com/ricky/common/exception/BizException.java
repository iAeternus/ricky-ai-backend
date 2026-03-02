package com.ricky.common.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detail;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = null;
    }

    public BizException(ErrorCode errorCode, String detail) {
        super(detail == null || detail.isBlank() ? errorCode.getMessage() : detail);
        this.errorCode = errorCode;
        this.detail = detail;
    }

}
