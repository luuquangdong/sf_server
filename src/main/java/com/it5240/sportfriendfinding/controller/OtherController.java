package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.dto.other.FileUpload;
import com.it5240.sportfriendfinding.model.atom.Media;
import com.it5240.sportfriendfinding.utils.Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/upload")
public class OtherController {
    @Autowired
    private Uploader uploader;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@ModelAttribute FileUpload fileUpload){
        Media image = uploader.uploadImage(fileUpload.getFile(), fileUpload.getType());
        return ResponseEntity.ok(image);
    }

    @PostMapping("/video")
    public ResponseEntity<?> uploadVideo(@ModelAttribute FileUpload fileUpload){
        Media video = uploader.uploadVideo(fileUpload.getFile(), "video");
        return ResponseEntity.ok(video);
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam String id){
        String[] type = id.split("/");

        uploader.deleteFile(id);
        return ResponseEntity.ok("{\"data\":\"OK\"}");
    }
}
