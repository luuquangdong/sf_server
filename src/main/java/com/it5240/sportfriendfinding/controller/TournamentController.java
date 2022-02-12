package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.unit.Media;
import com.it5240.sportfriendfinding.model.unit.TournamentBase;
import com.it5240.sportfriendfinding.model.unit.TournamentStatus;
import com.it5240.sportfriendfinding.model.dto.tournament.*;
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
import java.util.Map;

@Controller
@RequestMapping("/tournaments")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;

    @GetMapping
    public ResponseEntity<?> getListTournament(
            @RequestParam(required = false) ObjectId lastTournamentId,
            @RequestParam int size,
            Principal principal
    ){
        String meId = principal.getName();
        List<TournamentResp> tournament = tournamentService.getListTournamentV2(lastTournamentId, size, TournamentStatus.WAITING, meId);
        return ResponseEntity.ok(tournament);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyListTournament(Principal principal){
        String meId = principal.getName();
        List<TournamentResp> result = tournamentService.getMyListTournament(meId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/joined")
    public ResponseEntity<?> getMyListTournamentJoined(Principal principal){
        String meId = principal.getName();
        List<TournamentResp> tournament = tournamentService.getMyListTournamentJoined(meId);
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
        var result = tournamentService.deleteTournament(tournamentId, meId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-banner/{tournamentId}")
    public ResponseEntity<?> updateBanner(
            @PathVariable ObjectId tournamentId,
            @RequestParam("banner") MultipartFile banner,
            Principal principal
    ){
        String meId = principal.getName();
        Media result = tournamentService.uploadBanner(banner, tournamentId, meId);
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
        var result = tournamentService.requestToJoinTournament(tournamentId, meId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/answer-request")
    public ResponseEntity<?> confirmRequest(@RequestBody ConfirmRequest confirmRequest, Principal principal){
        String meId = principal.getName();
        Map<String, Object> result = tournamentService.confirmRequest(confirmRequest, meId);

        return ResponseEntity.ok(result);
    }

//    @PostMapping("/posts")
//    public ResponseEntity<?> createPost(
//            @Valid @ModelAttribute TournamentPostReq newPost,
//            Principal principal
//    ){
//        String meId = principal.getName();
//        TournamentPostResp postResponse = tournamentService.createPost(newPost, meId);
//        return ResponseEntity.ok(postResponse);
//    }
//
//    @GetMapping("/{tournamentId}/posts")
//    public ResponseEntity<?> getListPost(
//            Principal principal,
//            @PathVariable ObjectId tournamentId
//    ){
//        String meId = principal.getName();
//
//        List<TournamentPostResp> postsResponse = tournamentService.getListPost(meId, tournamentId);
//        return ResponseEntity.ok(postsResponse);
//    }
//
//    @GetMapping("/posts/like/{postId}")
//    public ResponseEntity<?> likePost(
//            @PathVariable ObjectId postId,
//            Principal principal
//    ){
//        String likerId = principal.getName();
//        TournamentPostResp postResponse = tournamentService.like(postId, likerId);
//
//        return ResponseEntity.ok(postResponse);
//    }
}
