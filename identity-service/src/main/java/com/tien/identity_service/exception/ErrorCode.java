package com.tien.identity_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "uncategorized error"),
    INVALID_KEY(1001, "invalid key error"),
    USER_EXISTED(1002, "user existed"),
    USERNAME_INVALID(1003, "username must be at least 3 characters"),
    INVALID_PASSWORD(1004, "password must be at least 3 characters"),
    USER_NOT_EXISTED(1005, "user not existed"),
    UNAUTHENTICATED(1006, "Unauthenticated"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

}
