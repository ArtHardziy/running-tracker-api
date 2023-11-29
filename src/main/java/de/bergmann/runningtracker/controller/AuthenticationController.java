package de.bergmann.runningtracker.controller;

import de.bergmann.runningtracker.model.dto.JwtAuthenticationResponse;
import de.bergmann.runningtracker.model.dto.SignInRequest;
import de.bergmann.runningtracker.model.dto.SignUpRequest;
import de.bergmann.runningtracker.service.AuthenticationService;
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