package com.it5240.sportfriend.model.exception;

public class AuthException extends BaseException {
    public AuthException(){
        super("Unauthorized", 1044);
    }
}
