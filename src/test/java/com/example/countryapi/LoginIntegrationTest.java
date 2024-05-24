package com.example.countryapi;

import com.example.countryapi.models.dto.RegisterRequestDto;
import com.example.countryapi.repository.UserRepository;
import com.example.countryapi.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;



    private MockMvc mockMvc;

    @BeforeAll
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        RegisterRequestDto user = new RegisterRequestDto("Test", "1234");
        authenticationService.register(user);
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteUserInfoByUsername("Test");
    }

    @Test
    void loginWithWrongUsername() throws Exception {
        RegisterRequestDto correctReq = new RegisterRequestDto("Test0", "1234");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(correctReq)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithWrongPassword() throws Exception {
        RegisterRequestDto correctReq = new RegisterRequestDto("Test", "incorrect");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(correctReq)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithInactiveUser() throws Exception {
        RegisterRequestDto correctReq = new RegisterRequestDto("Test", "1234");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(correctReq)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @Transactional
    void loginCorrectly() throws Exception {
        var userInfo = userRepository.findByUsername("Test");
        userInfo.get().setActive(true);
        userRepository.save(userInfo.get());

        RegisterRequestDto correctReq = new RegisterRequestDto("Test", "1234");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(correctReq)))
                .andExpect(status().isOk());
    }
}
