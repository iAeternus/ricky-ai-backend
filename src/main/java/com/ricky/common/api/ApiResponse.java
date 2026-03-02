package com.ricky.common.api;

import com.ricky.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiResponse<T> {
    int code;
    String message;
    T data;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .code(0)
                .message("ok")
                .data(data)
                .build();
    }

    public static ApiResponse<Void> ok() {
        return ApiResponse.<Void>builder()
                .code(0)
                .message("ok")
                .data(null)
                .build();
    }

    public static ApiResponse<Void> error(int code, String message) {
        return ApiResponse.<Void>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }

    public static ApiResponse<Void> error(ErrorCode errorCode, String message) {
        return ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(message)
                .data(null)
                .build();
    }
}
