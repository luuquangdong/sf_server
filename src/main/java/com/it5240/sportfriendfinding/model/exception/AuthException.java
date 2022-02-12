package com.it5240.sportfriendfinding.model.exception;

public class AuthException extends BaseException {
    public AuthException(){
        super("Unauthorized", 1044);
    }
}
