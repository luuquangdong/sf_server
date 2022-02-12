package com.it5240.sportfriendfinding.model.exception;

public class NotFoundException extends BaseException{
    public NotFoundException(String message, int code){
        super(message, code);
    }
}