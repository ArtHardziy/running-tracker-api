package de.bergmann.runnertracker.controller;

import de.bergmann.runnertracker.model.dto.JwtAuthenticationResponse;
import de.bergmann.runnertracker.model.dto.SignInRequest;
import de.bergmann.runnertracker.model.dto.SignUpRequest;
import de.bergmann.runnertracker.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse signup(@RequestBody SignInRequest request) {
        return authenticationService.signin(request);
    }
}