package com.example.countryapi.component;

import com.example.countryapi.models.Role;
import com.example.countryapi.models.UserInfo;
import com.example.countryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${config.admin.username}")
    private String adminUsername;

    @Value("${config.admin.password}")
    private String adminPassword;

    public CommandLineAppStartupRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String...args) throws Exception {
        if (userRepository.findByUsername(adminUsername).isPresent()) return;
        UserInfo admin = UserInfo.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
    }
}