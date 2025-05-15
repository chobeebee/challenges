package com.baro.challenges.user.config;

import com.baro.challenges.user.entity.UserEntity;
import com.baro.challenges.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Profile("test")
public class UserDataLoader {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository) {
        return args -> {
            if(userRepository.count() == 0){
                UserEntity master = UserEntity.builder()
                        .username("testmaster")
                        .password(passwordEncoder.encode("Test1234!@"))
                        .role(UserEntity.Role.MASTER)
                        .build();
                userRepository.save(master);

                UserEntity user1 = UserEntity.builder()
                        .username("testuser")
                        .password(passwordEncoder.encode("Test1234!@"))
                        .role(UserEntity.Role.USER)
                        .build();
                userRepository.save(user1);
            }
        };
    }
}
