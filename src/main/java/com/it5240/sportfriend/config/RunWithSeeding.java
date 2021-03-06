// package com.it5240.sportfriend.config;
//
// import com.it5240.sportfriend.model.entity.Sport;
// import com.it5240.sportfriend.model.entity.User;
// import com.it5240.sportfriend.repository.SportRepository;
// import com.it5240.sportfriend.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.security.crypto.password.PasswordEncoder;
//
// import java.util.List;
// import java.util.Optional;
//
// @SpringBootApplication(scanBasePackages = "com.it5240.sportfriendfinding")
// public class RunWithSeeding implements CommandLineRunner {
//     public static void main(String[] args) {
//         SpringApplication.run(com.it5240.sportfriendfinding.config.RunWithSeeding.class, args);
//     }
//
//     @Autowired
//     UserRepository userRepository;
//     @Autowired
//     PasswordEncoder passwordEncoder;
//     @Autowired
//     SportRepository sportRepository;
//
//     @Override
//     public void run(String... args) throws Exception {
//         Optional<User> adminOpt =  userRepository.findById("0123456789");
//         if(adminOpt.isEmpty()){
//             User admin = new User();
//             admin.setPhoneNumber("0123456789");
//             admin.setPassword(passwordEncoder.encode("admin123"));
//             admin.setName("Anh Min");
//             admin.setRole("ROLE_ADMIN");
//             userRepository.save(admin);
//         }
//         Optional<User> userOpt =  userRepository.findById("0333666999");
//         if(userOpt.isEmpty()){
//             User user = new User();
//             user.setPhoneNumber("0333666999");
//             user.setPassword(passwordEncoder.encode("user1234"));
//             user.setName("U So");
//             user.setRole(Role);
//             userRepository.save(user);
//         }
//
//         List<Sport> sportList = sportRepository.findAll();
//         if(sportList.isEmpty()){
//             for(int i=0; i<5; i++){
//                 sportList.add(new Sport());
//             }
//
//             sportList.get(0).setName("b??ng ????");
//             sportList.get(1).setName("b??ng r???");
//             sportList.get(2).setName("b??ng chuy???n");
//             sportList.get(3).setName("b??ng b??n");
//             sportList.get(4).setName("c???u l??ng");
//
//             sportRepository.saveAll(sportList);
//         }
//     }
// }
