package com.it5240.sportfriendfinding.config;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CommonConfig {

    @Bean
    public ModelMapper modelMapperConfig() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public Cloudinary cloudinaryConfig(){
        Map config = new HashMap();
        config.put("cloud_name", "dongcloud");
        config.put("api_key", "696816176637177");
        config.put("api_secret", "S0TmeE662Mj7gu-_Qtpy1HsqyZQ");
        Cloudinary cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
