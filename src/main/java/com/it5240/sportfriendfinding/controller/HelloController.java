package com.it5240.sportfriendfinding.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public ResponseEntity<?> helloWorld(){
        return ResponseEntity.ok("Hello World HiHi");
    }

}
