package com.it5240.sportfriend.controller;

import com.it5240.sportfriend.model.dto.auth.ChangePasswordInfo;
import com.it5240.sportfriend.model.dto.user.ListUserId;
import com.it5240.sportfriend.model.dto.user.SearchInfo;
import com.it5240.sportfriend.model.dto.user.UserReq;
import com.it5240.sportfriend.model.dto.user.UserResp;
import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.model.unit.Media;
import com.it5240.sportfriend.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id, Principal principal){
        String meId = principal.getName();
        UserResp userDto = userService.getUser(id, meId);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserInfo(@Valid @RequestBody UserReq newUserInfo, Principal principal){
        String phoneNumber = principal.getName();
        UserResp userDtoUpdated = userService.updateUserInfo(newUserInfo, phoneNumber);
        return ResponseEntity.ok(userDtoUpdated);
    }

    @PostMapping("/update-avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("avatarFile") MultipartFile avatarFile, Principal principal){
        String meId = principal.getName();
        Media result = userService.updateAvatar(avatarFile, meId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordInfo changePasswordInfo, Principal principal){
        String phoneNumber = principal.getName();
        var result = userService.changePassword(changePasswordInfo, phoneNumber);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getListUser(@RequestBody ListUserId data){
        List<User> result = userService.getListUser(data.getUserIds());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/find")
    public ResponseEntity<?> findFriends(@RequestBody SearchInfo searchInfo, Principal principal){
        String meId = principal.getName();
        List<UserResp> result = userService.suggestFriends(searchInfo, meId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }


    @Secured("ROLE_ADMIN")
    @GetMapping("/ban/{userId}/{banned}")
    public ResponseEntity<?> banUser(
            @PathVariable String userId,
            @PathVariable boolean banned
    ){
        return ResponseEntity.ok(userService.banUser(userId, banned));
    }

    @PostMapping("/signup-organization")
    public ResponseEntity<?> signupOrganization(@RequestParam MultipartFile idCardFile, Principal principal) {
        String meId = principal.getName();
        return ResponseEntity.ok(userService.signupOrganization(idCardFile, meId));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/signup-organization")
    public ResponseEntity<?> getAllSignupOrg() {
        return ResponseEntity.ok(userService.getAllSignupOrg());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/confirm-organization/{signupId}/{value}")
    public ResponseEntity<?> confirmOrganization(
            @PathVariable ObjectId signupId,
            @PathVariable boolean value
    ){
        return ResponseEntity.ok(userService.confirmOrganization(signupId, value));
    }

    @PostMapping("/set-push-token")
    public ResponseEntity<?> setPushToken(@RequestBody Map<String, String> data, Principal principal){
        String meId = principal.getName();
        String pushToken = data.get("pushToken");
        return ResponseEntity.ok(userService.setPushToken(pushToken, meId));
    }

    @DeleteMapping("/remove-push-token")
    public ResponseEntity<?> removePushToken(Principal principal){
        String meId = principal.getName();
        return ResponseEntity.ok(userService.removePushToken(meId));
    }
}
