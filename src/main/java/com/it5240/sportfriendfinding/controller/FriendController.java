package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.dto.user.FriendResp;
import com.it5240.sportfriendfinding.model.entity.FriendRequest;
import com.it5240.sportfriendfinding.service.FriendService;
import org.bson.types.ObjectId;
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
        List<FriendResp> friends = friendService.getListFriend(userId);
        return ResponseEntity.ok(friends);
    }


    @DeleteMapping("/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable(required = true) String friendId, Principal principal){
        String meId = principal.getName();
        String result = friendService.deleteFriend(meId, friendId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/answer-request/{requestId}/{answer}")
    public ResponseEntity<?> answerRequest(
            Principal principal,
            @PathVariable ObjectId requestId,
            @PathVariable int answer
    ) {
        String meId = principal.getName();
        String result = friendService.answerRequest(meId, requestId, answer == 1);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/request")
    public ResponseEntity<?> getListFriendRequest(Principal principal){
        String meId = principal.getName();
        List<FriendRequest> result = friendService.getListFriendRequest(meId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/request")
    public ResponseEntity<?> createRequest(@Valid @RequestBody FriendRequest friendRequest, Principal principal){
        String meId = principal.getName();
        friendRequest.setUserIdSent(meId);
        String result = friendService.createFriendRequest(friendRequest);
        return ResponseEntity.ok(result);
    }
}
