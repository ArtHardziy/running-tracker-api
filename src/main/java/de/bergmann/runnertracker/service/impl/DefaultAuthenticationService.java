package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.model.Role;
import de.bergmann.runnertracker.model.RoleType;
import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.RunningTrackerUserPrincipal;
import de.bergmann.runnertracker.model.dto.JwtAuthenticationResponse;
import de.bergmann.runnertracker.model.dto.SignInRequest;
import de.bergmann.runnertracker.model.dto.SignUpRequest;
import de.bergmann.runnertracker.service.AuthenticationService;
import de.bergmann.runnertracker.service.JwtService;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@AllArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {

    private RunningTrackerUserService runningTrackerUserService;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest signUpRequest) {
        var requestUsername = signUpRequest.getUsername();
        if (runningTrackerUserService.findUserByUsername(requestUsername).isPresent()) {
            throw new AuthorizationServiceException(String.format("User %s already exist!", requestUsername));
        }
        var role = new Role();
        role.setRoleType(RoleType.USER);
        var userToSignUp = RunnerUser
                .builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .sex(signUpRequest.getSex())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .birthDate(resolveBirthDate(signUpRequest.getBirthDate()))
                .role(role)
                .build();
        userToSignUp = runningTrackerUserService.save(userToSignUp);
        var jwt = jwtService.generateToken(RunningTrackerUserPrincipal.create(userToSignUp));
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        var signUpUser = runningTrackerUserService.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        var jwt = jwtService.generateToken(RunningTrackerUserPrincipal.create(signUpUser));
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    private LocalDate resolveBirthDate(String birthdateString) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(birthdateString, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Format of birth date must much 'dd-MM-yyyy'");
        }
    }
}