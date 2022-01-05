package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.dto.auth.AuthInfo;
import com.it5240.sportfriendfinding.model.dto.user.SearchInfo;
import com.it5240.sportfriendfinding.model.dto.user.UserReq;
import com.it5240.sportfriendfinding.model.dto.user.UserResp;
import com.it5240.sportfriendfinding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id){
        UserResp userDto = userService.getUser(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserInfo(@Valid @RequestBody UserReq newUserInfo, Principal principal){
        String phoneNumber = principal.getName();
        UserResp userDtoUpdated = userService.updateUserInfo(newUserInfo, phoneNumber);
        return ResponseEntity.ok(userDtoUpdated);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody AuthInfo authInfo, Principal principal){
        String phoneNumber = principal.getName();
        String result = userService.changePassword(authInfo, phoneNumber);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/find")
    public ResponseEntity<?> findFriends(@RequestBody SearchInfo searchInfo, Principal principal){
        String meId = principal.getName();
        List<UserResp> userResps = userService.findFriends(searchInfo, meId);
        return ResponseEntity.ok(userResps);
    }
}
