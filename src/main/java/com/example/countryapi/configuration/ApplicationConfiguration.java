package com.example.countryapi.configuration;

import com.example.countryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    @Value("${secrets.ninjasAPIKey}")
    private String APIKey;
    private final UserRepository userRepository;

    @Bean
    public RestTemplate ninjasRestTemplate() {
        return new RestTemplateBuilder(rt -> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("X-Api-Key", APIKey);
            return execution.execute(request, body);
        })).build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
   /* @Bean
    public UserRepository userRepository() {
        return new UserRepository() {
            @Override
            public <S extends UserInfo> S save(S entity) {
                return null;
            }

            @Override
            public <S extends UserInfo> Iterable<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public Optional<UserInfo> findById(Integer integer) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Integer integer) {
                return false;
            }

            @Override
            public Iterable<UserInfo> findAll() {
                return null;
            }

            @Override
            public Iterable<UserInfo> findAllById(Iterable<Integer> integers) {
                return null;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Integer integer) {

            }

            @Override
            public void delete(UserInfo entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Integer> integers) {

            }

            @Override
            public void deleteAll(Iterable<? extends UserInfo> entities) {

            }

            @Override
            public void deleteAll() {

            }
        };
    }*/
}
