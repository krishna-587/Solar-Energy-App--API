package com.example.Energy_Dashboard.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Energy_Dashboard.model.Role;
import com.example.Energy_Dashboard.model.User;
import com.example.Energy_Dashboard.repository.UserRepository;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("Krishna").isEmpty()) {
                User admin = new User();
                admin.setUsername("Krishna");
                admin.setEmailId("krishnagopalsingh587@gmail.com");
                admin.setPasswordHash(passwordEncoder.encode("Krishna@587"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}


