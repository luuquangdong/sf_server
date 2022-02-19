package com.it5240.sportfriend.model.dto.user;

import com.it5240.sportfriend.model.entity.Sport;
import com.it5240.sportfriend.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResp extends User {
    private List<Sport> sports;
    private boolean canEdit;
    private boolean isFriend;
    private boolean isRequestedFriend;
}
