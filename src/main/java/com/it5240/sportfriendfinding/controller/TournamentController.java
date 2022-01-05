package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.atom.TournamentBase;
import com.it5240.sportfriendfinding.model.atom.TournamentStatus;
import com.it5240.sportfriendfinding.model.dto.tournament.ConfirmRequest;
import com.it5240.sportfriendfinding.model.dto.tournament.ScheduleInfo;
import com.it5240.sportfriendfinding.model.dto.tournament.TournamentResp;
import com.it5240.sportfriendfinding.model.entity.Tournament;
import com.it5240.sportfriendfinding.service.TournamentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/tournaments")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;

    @GetMapping
    public ResponseEntity<?> getListTournament(
            @RequestParam(required = false) ObjectId lastId,
            @RequestParam int size,
            Principal principal
    ){
        String meId = principal.getName();
        List<TournamentResp> tournament = tournamentService.getListTournament(lastId, size, TournamentStatus.WAITING, meId);
        return ResponseEntity.ok(tournament);
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<?> getTournament(@PathVariable ObjectId tournamentId, Principal principal){
        String meId = principal.getName();
        TournamentResp tournament = tournamentService.getTournament(tournamentId, meId);
        return ResponseEntity.ok(tournament);
    }

    @PostMapping
    public ResponseEntity<?> createTournament(@Valid @RequestBody Tournament tournament, Principal principal){
        String meId = principal.getName();
        TournamentResp tournamentResp = tournamentService.createTournament(tournament, meId);
        return ResponseEntity.ok(tournamentResp);
    }

    @PutMapping("/{tournamentId}")
    public ResponseEntity<?> updateTournament(@Valid @RequestBody Tournament tournament) {
        return null;
    }

    @DeleteMapping("/{tournamentId}")
    public ResponseEntity<?> deleteTournament(@PathVariable ObjectId tournamentId, Principal principal){
        String meId = principal.getName();
        String result = tournamentService.deleteTournament(tournamentId, meId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-banner/{tournamentId}")
    public ResponseEntity<?> updateBanner(
            @PathVariable ObjectId tournamentId,
            @RequestParam("banner") MultipartFile banner,
            Principal principal
    ){
        String meId = principal.getName();
        String result = tournamentService.uploadBanner(banner, tournamentId, meId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-info/{tournamentId}")
    public ResponseEntity<?> updateInfo(@Valid @RequestBody TournamentBase tournamentInfo, Principal principal){
        String meId = principal.getName();
        TournamentResp tournamentResp = tournamentService.updateInfo(tournamentInfo, meId);
        return ResponseEntity.ok(tournamentResp);
    }

    @PutMapping("/update-schedule/{tournamentId}")
    public ResponseEntity<?> updateSchedule(@RequestBody ScheduleInfo scheduleInfo, Principal principal){
        String meId = principal.getName();
        TournamentResp tournamentResp = tournamentService.updateSchedule(scheduleInfo, meId);
        return ResponseEntity.ok(tournamentResp);
    }

    @GetMapping("/join/{tournamentId}")
    public ResponseEntity<?> requestToJoinTournament(Principal principal, @PathVariable ObjectId tournamentId){
        String meId = principal.getName();
        String result = tournamentService.requestToJoinTournament(tournamentId, meId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmRequest(@RequestBody ConfirmRequest confirmRequest, Principal principal){
        String meId = principal.getName();
        String result = tournamentService.confirmRequest(confirmRequest, meId);

        return ResponseEntity.ok(result);
    }
}
