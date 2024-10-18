package io.renatofreire.core.controller;

import io.renatofreire.core.dto.reponses.AuthenticationResponse;
import io.renatofreire.core.dto.requests.AuthenticationRequest;
import io.renatofreire.core.dto.requests.RegistrationRequest;
import io.renatofreire.core.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid final RegistrationRequest registrationRequest) {
        authenticationService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Boolean> verifyEmail(@RequestParam(name = "token", required = true) final String token) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.verifyToken(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(authenticationRequest));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.refreshToken(request, response));
    }


}
