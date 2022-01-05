package com.it5240.sportfriendfinding.exception;

import com.it5240.sportfriendfinding.exception.model.ExceptionType;
import com.it5240.sportfriendfinding.exception.model.InvalidException;

public class InvalidExceptionFactory {
    private InvalidExceptionFactory(){};

    public static final InvalidException get(ExceptionType type){
        switch (type){
            case UNAUTHORIZED:
                return new InvalidException("Unauthorized", 1043);
            case PARAMETER_NOT_ENOUGH:
                return new InvalidException("Parameter is not enough", 1010);
            case PHONE_NUMBER_EXISTED:
                return new InvalidException("Phone number is existed", 1010);
        }
        return new InvalidException("Something wrong", 1100);
    }
}
