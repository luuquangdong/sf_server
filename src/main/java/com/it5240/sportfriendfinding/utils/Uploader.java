package com.it5240.sportfriendfinding.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.it5240.sportfriendfinding.model.atom.Media;
import com.it5240.sportfriendfinding.exception.model.InvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Component
public class Uploader {

    @Autowired
    private Cloudinary cloudinary;

    public Media uploadImage(MultipartFile image, String folder){
        if(!image.getContentType().startsWith("image")){
            throw new InvalidException("Avatar must be image", 1011);
        }
        Map<String, String> options = new HashMap<>();
        options.put("folder", folder);
        Media result = upload(image, options);

        return result;
    }

    public Media uploadVideo(MultipartFile video, String folder){
        if(!video.getContentType().startsWith("video")){
            throw new InvalidException("It not a video", 1012);
        }
        Map<String, String> options = new HashMap<>();
        options.put("folder", folder);
        options.put("resource_type", "video");
        Media result = upload(video, options);
        return result;
    }

    public Media upload(MultipartFile media, Map<String, String> options) {
        Media result = null;
        try {
            Map<String, Object> response = cloudinary
                    .uploader()
                    .upload(
                            media.getBytes(),
                            options
                    );
            result = new Media((String) response.get("public_id"), (String) response.get("url"));
        } catch (Exception e){
            throw new InvalidException("Upload failed", 1013);
        }
        return result;
    }

    public void deleteFile(String id){
        try{
            Map<String, String> options = null;
            if(id.startsWith("video")){
                options = ObjectUtils.asMap("resource_type", "video");
            } else {
                options = ObjectUtils.emptyMap();
            }
            cloudinary.uploader().destroy(id, options);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}