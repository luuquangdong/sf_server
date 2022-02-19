package com.it5240.sportfriend.config;

import com.it5240.sportfriend.model.entity.Sport;
import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.model.unit.Role;
import com.it5240.sportfriend.repository.SportRepository;
import com.it5240.sportfriend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

     @Autowired
     private UserRepository userRepository;
     @Autowired
     private PasswordEncoder passwordEncoder;
     @Autowired
     private SportRepository sportRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Optional<User> adminOpt =  userRepository.findById("0123456789");
         if(adminOpt.isEmpty()){
             User admin = new User();
             admin.setPhoneNumber("0123456789");
             admin.setPassword(passwordEncoder.encode("admin123"));
             admin.setName("Anh Min");
             admin.setRole(Role.ROLE_ADMIN);
             userRepository.save(admin);
         }

         List<Sport> sportList = sportRepository.findAll();
         if(sportList.isEmpty()){
             for(int i=0; i<5; i++){
                 sportList.add(new Sport());
             }

             sportList.get(0).setName("bóng đá");
             sportList.get(1).setName("bóng rổ");
             sportList.get(2).setName("bóng chuyền");
             sportList.get(3).setName("bóng bàn");
             sportList.get(4).setName("cầu lông");

             sportRepository.saveAll(sportList);
         }
    }
}
