package com.it5240.sportfriendfinding.exception;

import com.it5240.sportfriendfinding.model.exception.ExceptionType;
import com.it5240.sportfriendfinding.model.exception.NotFoundException;

public class NotFoundExceptionFactory {

    private NotFoundExceptionFactory(){}

    public static final NotFoundException get(ExceptionType type){
        switch (type){
            case TOURNAMENT_NOT_FOUND:
                return new NotFoundException("Tournament is not existed", 1010);
            case ROOM_NOT_FOUND:
                return new NotFoundException("Room is not existed", 1011);
            case POST_NOT_FOUND:
                return new NotFoundException("Post is not existed", 1012);
            case COMMENT_NOT_FOUND:
                return new NotFoundException("Comment is not existed", 1013);
            case USER_NOT_FOUND:
                return new NotFoundException("User is not existed", 1014);
            case FRIEND_REQUEST_NOT_FOUND:
                return new NotFoundException("Friend request is not existed", 1015);
            case SPORT_NOT_FOUND:
                return new NotFoundException("Sport is not existed", 1016);
            case TOURNAMENT_POST_NOT_FOUND:
                return new NotFoundException("Post is not existed", 1017);
        }
        return new NotFoundException("Not found", 1050);
    }
}
