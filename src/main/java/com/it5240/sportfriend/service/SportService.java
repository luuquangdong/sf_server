package com.it5240.sportfriend.service;

import com.it5240.sportfriend.exception.NotFoundExceptionFactory;
import com.it5240.sportfriend.model.exception.ExceptionType;
import com.it5240.sportfriend.model.entity.Sport;
import com.it5240.sportfriend.repository.SportRepository;
import com.it5240.sportfriend.utils.RespHelper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SportService {
    @Autowired
    private SportRepository sportRepository;

    public Sport createSport(Sport sport){
        sport.setId(null);
        sport.setName(sport.getName().toLowerCase(Locale.ROOT));
        return sportRepository.save(sport);
    }

    public Sport updateSport(Sport sport){
        sportRepository.findById(sport.getId())
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.SPORT_NOT_FOUND));
        sport.setName(sport.getName().toLowerCase(Locale.ROOT));
        return sportRepository.save(sport);
    }

    public List<Sport> getListSport(){
        return sportRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Sport::getName))
                .collect(Collectors.toList());
    }

    public Map<String, Object> deleteSport(ObjectId sportId){
        sportRepository.deleteById(sportId);
        return RespHelper.ok();
    }
}
