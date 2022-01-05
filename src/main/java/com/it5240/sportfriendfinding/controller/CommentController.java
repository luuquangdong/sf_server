package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.dto.post.CommentResp;
import com.it5240.sportfriendfinding.model.entity.Comment;
import com.it5240.sportfriendfinding.service.CommentService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment newComment, Principal principal){
        String authorId = principal.getName();
        CommentResp comment = commentService.createComment(newComment, authorId);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable ObjectId commentId, Principal principal){
        String authorId = principal.getName();
        String result = commentService.deleteComment(commentId, authorId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getListComment(
            Principal principal,
            @RequestParam ObjectId postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        String meId = principal.getName();
        List<CommentResp> response = commentService.getListComment(postId, meId, page, size);
        return ResponseEntity.ok(response);
    }
}