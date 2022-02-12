package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.unit.Media;
import com.it5240.sportfriendfinding.model.dto.auth.ChangePasswordInfo;
import com.it5240.sportfriendfinding.model.dto.user.ListUserId;
import com.it5240.sportfriendfinding.model.dto.user.SearchInfo;
import com.it5240.sportfriendfinding.model.dto.user.UserReq;
import com.it5240.sportfriendfinding.model.dto.user.UserResp;
import com.it5240.sportfriendfinding.model.entity.User;
import com.it5240.sportfriendfinding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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
}
