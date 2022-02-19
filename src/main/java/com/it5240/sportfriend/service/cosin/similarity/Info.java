package com.it5240.sportfriend.service.cosin.similarity;

import com.it5240.sportfriend.model.unit.Gender;
import com.it5240.sportfriend.model.unit.Location;
import com.it5240.sportfriend.model.dto.user.SearchInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Info {
    private String name;
    private Set<ObjectId> sportIds;
    private Location location;
    private Gender gender;
    private int age;

    public Info(SearchInfo searchInfo) {
        this.name = searchInfo.getName();
        this.sportIds = searchInfo.getSportIds();
        this.location = searchInfo.getLocation();
        this.gender = searchInfo.getGender();
        // this.age = (searchInfo.getFromAge() + searchInfo.getToAge())/2;
        this.age = searchInfo.getAge();
    }
}
