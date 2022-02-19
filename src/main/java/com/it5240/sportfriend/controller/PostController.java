package com.it5240.sportfriend.controller;

import com.it5240.sportfriend.model.dto.post.PostReq;
import com.it5240.sportfriend.model.dto.post.PostResp;
import com.it5240.sportfriend.model.dto.tournament.TournamentPostReq;
import com.it5240.sportfriend.model.entity.ReportPost;
import com.it5240.sportfriend.service.PostService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @ModelAttribute PostReq newPost, Principal principal){
        String phoneNumber = principal.getName();
        PostResp postResponse = postService.createPost(newPost, phoneNumber);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping
    public ResponseEntity<?> getListPost(
            Principal principal,
            @RequestParam(required = false) ObjectId lastId,
            @RequestParam int size
            ){
        String phoneNumber = principal.getName();

        List<PostResp> postsResponse = postService.getListPost(phoneNumber, lastId, size);
        return ResponseEntity.ok(postsResponse);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable ObjectId postId){
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/user/{posterId}")
    public ResponseEntity<?> getListPostOfUser(@PathVariable String posterId, Principal principal){
        String meId = principal.getName();
        List<PostResp> postsResponse = postService.getListPostOfUser(posterId, meId);
        return ResponseEntity.ok(postsResponse);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable ObjectId postId, Principal principal){
        String phoneNumber = principal.getName();
        var result = postService.deletePost(phoneNumber, postId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePost(
///            @PathVariable ObjectId postId,
            @Valid @ModelAttribute PostReq newPost,
            Principal principal
    ){
        String phoneNumber = principal.getName();
        PostResp result = postService.updatePost(newPost, phoneNumber);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable ObjectId postId, Principal principal){
        String likerId = principal.getName();
        PostResp postResponse = postService.like(postId, likerId);

        return ResponseEntity.ok(postResponse);
    }

    @PostMapping("/tournament")
    public ResponseEntity<?> createTournamentPost(
            @Valid @ModelAttribute TournamentPostReq newPost,
            Principal principal
    ) {
        String meId = principal.getName();
        return ResponseEntity.ok(postService.createTournamentPost(newPost, meId));
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<?> getListTournamentPost(
            Principal principal,
            @PathVariable ObjectId tournamentId
    ){
        String meId = principal.getName();
        return ResponseEntity.ok(postService.getListTournamentPost(tournamentId, meId));
    }

    @DeleteMapping("/tournament/{tournamentId}/{postId}")
    public ResponseEntity<?> deleteTournamentPost(
            Principal principal,
            @PathVariable ObjectId tournamentId,
            @PathVariable ObjectId postId
    ){
        String meId = principal.getName();
        return ResponseEntity.ok(postService.deleteTournamentPost(tournamentId, postId, meId));
    }

    @PostMapping("/report")
    public ResponseEntity<?> reportPost(
            Principal principal,
            @RequestBody ReportPost reportPost
    ){
        String meId = principal.getName();
        return ResponseEntity.ok(postService.reportPost(reportPost, meId));
    }

    @GetMapping("/list-report")
    public ResponseEntity<?> getAllReport(){
        return ResponseEntity.ok(postService.getAllReport());
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/report/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable ObjectId reportId){
        return ResponseEntity.ok(postService.deleteReport(reportId));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(postService.getAll());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/ban/{postId}/{value}")
    public ResponseEntity<?> banPost(
            @PathVariable ObjectId postId,
            @PathVariable boolean value
    ){
        return ResponseEntity.ok(postService.banPost(postId, value));
    }
}
