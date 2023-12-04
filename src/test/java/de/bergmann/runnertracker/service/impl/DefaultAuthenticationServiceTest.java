package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.Sex;
import de.bergmann.runnertracker.model.dto.JwtAuthenticationResponse;
import de.bergmann.runnertracker.model.dto.SignInRequest;
import de.bergmann.runnertracker.model.dto.SignUpRequest;
import de.bergmann.runnertracker.service.JwtService;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.Optional;

import static de.bergmann.runnertracker.TestUtils.setUpTestRunnerWithRun;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAuthenticationServiceTest {

    @Mock
    RunningTrackerUserService runningTrackerUserService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    DefaultAuthenticationService defaultAuthenticationService;
    SignUpRequest signUpRequest;
    SignInRequest signInRequest;
    RunnerUser runnerUser;
    JwtAuthenticationResponse testJwtAuthenticationResponse;

    @BeforeEach
    public void setUp() {
        this.defaultAuthenticationService = new DefaultAuthenticationService(runningTrackerUserService, passwordEncoder, jwtService, authenticationManager);
        this.runnerUser = setUpTestRunnerWithRun();
        this.signUpRequest = SignUpRequest.builder()
                .username("test")
                .password("test")
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .birthDate("07-05-2002")
                .sex(Sex.THEY)
                .build();
        this.signInRequest = SignInRequest.builder()
                .username("test")
                .password("test")
                .build();
        this.testJwtAuthenticationResponse = JwtAuthenticationResponse.builder().token("jwt-token").build();
    }

    @Test
    void signupShouldThrowAuthorizationException() {
        when(runningTrackerUserService.findUserByUsername("test")).thenReturn(Optional.of(new RunnerUser()));
        assertThrows(AuthorizationServiceException.class,
                () -> defaultAuthenticationService.signup(this.signUpRequest));
    }

    @Test
    void signup() {
        when(runningTrackerUserService.findUserByUsername("test"))
                .thenReturn(Optional.empty());
        when(runningTrackerUserService.save(any()))
                .thenReturn(runnerUser);
        when(jwtService.generateToken(any())).thenReturn("jwt-token");
        assertEquals(this.testJwtAuthenticationResponse, defaultAuthenticationService.signup(this.signUpRequest));
    }

    @Test
    void signupShouldThrowResponseStatusException_whenInvalidBirthdate() {
        when(runningTrackerUserService.findUserByUsername("test"))
                .thenReturn(Optional.empty());
        this.signUpRequest.setBirthDate("invalid-format");
        assertThrows(ResponseStatusException.class, () -> defaultAuthenticationService.signup(signUpRequest));
    }

    @Test
    void signinShouldThrowResponseStatusException() {
        when(runningTrackerUserService.findUserByUsername("test"))
                .thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> defaultAuthenticationService.signin(this.signInRequest));
    }

    @Test
    void signinShouldReturnJwt() {
        when(runningTrackerUserService.findUserByUsername("test"))
                .thenReturn(Optional.of(runnerUser));

        when(jwtService.generateToken(any()))
                .thenReturn("jwt-token");
        assertEquals(JwtAuthenticationResponse.builder().token("jwt-token").build(),defaultAuthenticationService.signin(signInRequest) );
    }
}