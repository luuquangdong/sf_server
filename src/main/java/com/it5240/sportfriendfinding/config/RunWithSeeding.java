package com.it5240.sportfriendfinding.config;

import com.it5240.sportfriendfinding.entity.User;
import com.it5240.sportfriendfinding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication(scanBasePackages = "com.it5240.sportfriendfinding")
public class RunWithSeeding implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(com.it5240.sportfriendfinding.config.RunWithSeeding.class, args);
    }

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> adminOpt =  userRepository.findById("0123456789");
        if(adminOpt.isEmpty()){
            User admin = new User();
            admin.setPhoneNumber("0123456789");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Anh Min");
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }
        Optional<User> userOpt =  userRepository.findById("0333666999");
        if(userOpt.isEmpty()){
            User user = new User();
            user.setPhoneNumber("0333666999");
            user.setPassword(passwordEncoder.encode("user1234"));
            user.setFullName("U So");
            user.setRole("ROLE_USER");
            userRepository.save(user);
        }
    }
}
