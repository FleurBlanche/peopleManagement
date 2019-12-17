package com.hjq.demo.common.response;

import lombok.Getter;

public enum  ResultCode {
    NO_SUITABLE_OBJECT(1, "no suitable object found"),
    SUCCESS(0, "success"),
    ERROR(-1, "error"),
    OBJECT_NOT_FOUND(-2, "%s not found"),
    OBJECT_ALREADY_EXISTS(-3, "%s already exists"),
    FAILURE(-4, "failure"),
    ILLEGAL_ARGUMENT(-5, "argument %s is illegal because %s"),
    RESOURCE_NOT_MATCH(-6, "resource %s not match"),
    DUPLICATE_KEY(-7, "duplicate key"),
    TIME_RANGE_OCCUPIED(-8, "Time %s occupied by other meeting"),
    SERVICE_INVOKED_FAILURE(-9, "%s invoked failure, reason: %s"),
    DATE_CONVERT_FAILURE(-10, "time format %s can't be converted to date"),
    FEIGN_EXCEPTION(-11, "feign exception");

    @Getter
    private int code;

    @Getter
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
