package com.it5240.sportfriendfinding.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidException extends RuntimeException{
    private int code;

    public InvalidException(String message, int code){
        super(message);
        this.code = code;
    }
}