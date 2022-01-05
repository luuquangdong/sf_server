package com.it5240.sportfriendfinding.repository.dao;

import com.it5240.sportfriendfinding.model.dto.user.SearchInfo;
import com.it5240.sportfriendfinding.model.entity.User;
import com.it5240.sportfriendfinding.model.atom.Location;
import com.it5240.sportfriendfinding.model.atom.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class UserDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> findFriends(SearchInfo searchInfo){
        Criteria criteria = Criteria.where("name").regex(String.format(".*%s.*", searchInfo.getName()));
        criteria.and("role").ne(Role.ROLE_ADMIN.toString());

        Location location = searchInfo.getLocation();
        setLocationCriteria(criteria, location);

        setAgeRangeCriteria(criteria, searchInfo.getFromAge(), searchInfo.getToAge());

        if(searchInfo.getGender() != null){
            criteria.and("gender").is(searchInfo.getGender().toString());
        }
        if(searchInfo.getSportIds() != null && searchInfo.getSportIds().size() != 0){
            criteria.and("sportIds").in(searchInfo.getSportIds());
        }
        Query query = Query.query(criteria);
        query.with(Sort.by(Sort.Direction.DESC, "lastModified"));
        query.skip(searchInfo.getIndex());
        query.limit(searchInfo.getSize());
        return mongoTemplate.find(query, User.class);
    };

    private void setLocationCriteria(Criteria criteria, Location location){
        if(location == null) return;
        if(location.getProvince() != null) {
            criteria.and("location.province").is(location.getProvince());

            if(location.getDistrict() != null) {
                criteria.and("location.district").is(location.getDistrict());
            }
        }
    }

    private void setAgeRangeCriteria(Criteria criteria, int fromAge, int toAge){
        LocalDate now = LocalDate.now();
        LocalDate minAge = now.minusYears(toAge);
        LocalDate maxAge = now.minusYears(fromAge);
        criteria.and("birthday").gte(minAge).lte(maxAge);
    }

}
