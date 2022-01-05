package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.dto.post.PostReq;
import com.it5240.sportfriendfinding.model.dto.post.PostResp;
import com.it5240.sportfriendfinding.service.PostService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            @RequestParam(required = false) ObjectId lastPostId,
            @RequestParam int size
            ){
        String phoneNumber = principal.getName();
        List<PostResp> postsResponse = postService.getListPost(phoneNumber, lastPostId, size);
        return ResponseEntity.ok(postsResponse);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable ObjectId postId, Principal principal){
        String phoneNumber = principal.getName();
        String result = postService.deletePost(phoneNumber, postId);
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

}
