package com.it5240.sportfriendfinding.model.dto.user;

import com.it5240.sportfriendfinding.model.unit.Gender;
import com.it5240.sportfriendfinding.model.unit.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class SearchInfo {

    private String name;
    private Location location;
    private Gender gender;
    private int fromAge;
    private int toAge;
    private int age;
    private Set<ObjectId> sportIds;
    private int index;
    private int size;

    public SearchInfo(){
        name = "";
        location = null;
        gender = null;
        sportIds = new HashSet<>();
        fromAge = 1;
        toAge = 99;
        index = 0;
        size = 20;
    }
}
