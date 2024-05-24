package com.example.countryapi;

import com.example.countryapi.models.Token;
import com.example.countryapi.models.dto.RegisterRequestDto;
import com.example.countryapi.repository.TokenRepository;
import com.example.countryapi.repository.UserRepository;
import com.example.countryapi.services.AuthenticationService;
import com.example.countryapi.services.JwtService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CountryAndWeatherApiTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvc;

    @BeforeAll
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        RegisterRequestDto userReq = new RegisterRequestDto("Test", "1234");
        var userInfo = authenticationService.register(userReq);
        var date = Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant());
        String jwtToken = jwtService.generateToken(userInfo, date);
        Token test_token = Token.builder().name("test_token").token(jwtToken).expireDate(date).userInfo(userInfo).build();
        tokenRepository.save(test_token);
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteUserInfoByUsername("Test");
    }

    @Test
    void getCountryInfoTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/countries/iran"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Iran, Islamic Republic Of")));
    }

    @Test
    void getCountryListTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/countries"))
                .andExpect(status().isOk());
    }

    @Test
    void getWeatherInfoTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/countries/iran/weather"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capital", is("Tehran")));
    }
}
