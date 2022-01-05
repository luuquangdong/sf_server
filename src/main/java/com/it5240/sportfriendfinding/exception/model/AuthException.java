package com.it5240.sportfriendfinding.exception.model;

public class AuthException extends BaseException {
    public AuthException(){
        super("Unauthorized", 1044);
    }
}
