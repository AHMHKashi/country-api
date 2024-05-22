package com.example.countryapi.component;

import com.example.countryapi.models.Role;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String...args) throws Exception {
        UserInfo admin = UserInfo.builder()
                .username("mohsen")
                .password(passwordEncoder.encode("123456"))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
    }
}