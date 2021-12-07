package com.it5240.sportfriendfinding.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException{

    private int code;

    public NotFoundException(String message, int code){
        super(message);
        this.code = code;
    }
}