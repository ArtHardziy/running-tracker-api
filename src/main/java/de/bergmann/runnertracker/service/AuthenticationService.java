package de.bergmann.runnertracker.service;

import de.bergmann.runnertracker.model.dto.JwtAuthenticationResponse;
import de.bergmann.runnertracker.model.dto.SignInRequest;
import de.bergmann.runnertracker.model.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SignInRequest request);
}
