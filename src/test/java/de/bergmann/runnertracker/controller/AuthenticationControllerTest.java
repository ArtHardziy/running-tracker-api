package de.bergmann.runnertracker.controller;

import de.bergmann.runnertracker.filters.JwtAuthenticationFilter;
import de.bergmann.runnertracker.model.Sex;
import de.bergmann.runnertracker.model.dto.JwtAuthenticationResponse;
import de.bergmann.runnertracker.model.dto.SignInRequest;
import de.bergmann.runnertracker.model.dto.SignUpRequest;
import de.bergmann.runnertracker.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static de.bergmann.runnertracker.TestUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtAuthenticationFilter authenticationFilter;

    @Test
    void singup() throws Exception {
        var signUpRequest = SignUpRequest.builder()
                .username("test")
                .email("test@test.com")
                .password("test")
                .lastName("test")
                .firstName("test")
                .sex(Sex.THEY)
                .birthDate("07-05-2002")
                .build();
        var expectedJson = "{\"token\":\"test-token\"}";
        given(this.authenticationService.signup(any()))
                .willReturn(new JwtAuthenticationResponse("test-token"));
        this.mockMvc.perform(post("/v1/signup")
                        .content(asJsonString(signUpRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }

    @Test
    void singin() throws Exception {
        var signInRequest = SignInRequest.builder()
                .username("test")
                .password("test")
                .build();
        var expectedJson = "{\"token\":\"test-token\"}";
        given(this.authenticationService.signin(any()))
                .willReturn(new JwtAuthenticationResponse("test-token"));
        this.mockMvc.perform(post("/v1/signin")
                .content(asJsonString(signInRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));
    }
}