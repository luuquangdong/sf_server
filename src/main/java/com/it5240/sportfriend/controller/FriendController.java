package com.it5240.sportfriend.controller;

import com.it5240.sportfriend.model.dto.user.ShortInfoUser;
import com.it5240.sportfriend.model.entity.FriendRequest;
import com.it5240.sportfriend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getFriends(@PathVariable(required = true) String userId){
        List<ShortInfoUser> friends = friendService.getListFriend(userId);
        return ResponseEntity.ok(friends);
    }


    @DeleteMapping("/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable(required = true) String friendId, Principal principal){
        String meId = principal.getName();
        var result = friendService.deleteFriend(meId, friendId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/answer-request/{requesterId}/{answer}")
    public ResponseEntity<?> answerRequest(
            Principal principal,
            @PathVariable String requesterId,
            @PathVariable boolean answer
    ) {
        String meId = principal.getName();
        var result = friendService.answerRequest(meId, requesterId, answer);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/request")
    public ResponseEntity<?> getListFriendRequest(Principal principal){
        String meId = principal.getName();
        List<ShortInfoUser> result = friendService.getListFriendRequest(meId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestMakeFriend(@Valid @RequestBody FriendRequest friendRequest, Principal principal){
        String meId = principal.getName();
        var result = friendService.requestMakeFriend(friendRequest, meId);
        return ResponseEntity.ok(result);
    }
}
