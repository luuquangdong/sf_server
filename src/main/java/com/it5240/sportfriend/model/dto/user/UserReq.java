package com.it5240.sportfriend.model.dto.user;

import com.it5240.sportfriend.model.unit.UserBase;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserReq extends UserBase {
    private List<ObjectId> sportIds;
}
