package com.noosyn.onboarding.utils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.noosyn.onboarding.entity.Role;
import com.noosyn.onboarding.entity.User;
import com.noosyn.onboarding.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public void run(ApplicationArguments args) {
        if (repo.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(encoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();

            repo.save(admin);
            System.out.println("ADMIN created: admin / admin123");
        }
    }
}
 
