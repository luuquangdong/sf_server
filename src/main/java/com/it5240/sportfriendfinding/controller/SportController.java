package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.entity.Sport;
import com.it5240.sportfriendfinding.service.SportService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sports")
public class SportController {
    @Autowired
    private SportService sportService;

    @GetMapping
    public ResponseEntity<?> getListSport(){
        List<Sport> sports = sportService.getListSport();
        return ResponseEntity.ok(sports);
    }

    @PostMapping
    public ResponseEntity<?> createSport(@RequestBody Sport sport){
        Sport sportCreated = sportService.createSport(sport);
        return ResponseEntity.ok(sportCreated);
    }

    @PutMapping("/{sportId}")
    public ResponseEntity<?> updateSport(@RequestBody Sport sport){
        Sport sportUpdated = sportService.updateSport(sport);
        return ResponseEntity.ok(sportUpdated);
    }

    @DeleteMapping("/{sportId}")
    public ResponseEntity<?> deleteSport(@PathVariable ObjectId sportId){
        return ResponseEntity.ok(sportService.deleteSport(sportId));
    }
}
