package com.it5240.sportfriend.model.exception;


public class InvalidException extends BaseException{
    public InvalidException(String message, int code){
        super(message, code);
    }
}