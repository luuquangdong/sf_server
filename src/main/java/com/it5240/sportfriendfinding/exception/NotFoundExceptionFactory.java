package com.it5240.sportfriendfinding.exception;

import com.it5240.sportfriendfinding.exception.model.ExceptionType;
import com.it5240.sportfriendfinding.exception.model.NotFoundException;

public class NotFoundExceptionFactory {

    private NotFoundExceptionFactory(){}

    public static final NotFoundException get(ExceptionType type){
        switch (type){
            case TOURNAMENT_NOT_FOUND:
                return new NotFoundException("Tournament is not existed", 1035);
            case ROOM_NOT_FOUND:
                return new NotFoundException("Room is not existed", 1030);
            case POST_NOT_FOUND:
                return new NotFoundException("Post is not existed", 1015);
            case COMMENT_NOT_FOUND:
                return new NotFoundException("Comment is not existed", 1016);
            case USER_NOT_FOUND:
                return new NotFoundException("User is not existed", 1002);
            case FRIEND_REQUEST_NOT_FOUND:
                return new NotFoundException("Friend request is not existed", 1032);
            case SPORT_NOT_FOUND:
                return new NotFoundException("Sport is not existed", 1031);
        }
        return new NotFoundException("Not found", 1050);
    }
}
