package com.doubleowner.revibe.global.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class CommonResponseBody<T> {
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public CommonResponseBody(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public CommonResponseBody(String message) {
        this(message, null);
    }
}