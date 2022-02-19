package com.it5240.sportfriend.exception;

import com.it5240.sportfriend.model.exception.ExceptionType;
import com.it5240.sportfriend.model.exception.InvalidException;

public class InvalidExceptionFactory {
    private InvalidExceptionFactory(){};

    public static final InvalidException get(ExceptionType type){
        switch (type){
            case PARAMETER_NOT_ENOUGH:
                return new InvalidException("Parameter is not enough", 1101);
            case PHONE_NUMBER_EXISTED:
                return new InvalidException("Phone number is existed", 1102);
            case FILE_IS_NOT_IMAGE:
                return new InvalidException("File must be image", 1103);
            case UNAUTHORIZED:
                return new InvalidException("Not allow", 1104);
            case PASSWORD_NOT_MATCH:
                return new InvalidException("Password not match", 1105);
        }
        return new InvalidException("Something wrong", 1100);
    }
}
