package com.it5240.sportfriendfinding.model.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    protected int code;
    public BaseException(String message, int code) {
        super(message);
        this.code = code;
    }
}
