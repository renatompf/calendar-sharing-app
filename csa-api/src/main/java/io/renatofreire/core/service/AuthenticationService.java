package io.renatofreire.core.service;

import io.renatofreire.core.dto.requests.RegisteredUserMessageDto;
import io.renatofreire.core.dto.reponses.AuthenticationResponse;
import io.renatofreire.core.dto.requests.AuthenticationRequest;
import io.renatofreire.core.dto.requests.RegistrationRequest;
import io.renatofreire.core.enums.TokenType;
import io.renatofreire.core.exceptions.EmailAlreadyValidatedException;
import io.renatofreire.core.exceptions.InvalidFieldException;
import io.renatofreire.core.exceptions.TokenAlreadyExpiredException;
import io.renatofreire.core.model.Token;
import io.renatofreire.core.model.User;
import io.renatofreire.core.model.VerificationToken;
import io.renatofreire.core.repository.RoleRepository;
import io.renatofreire.core.repository.TokenRepository;
import io.renatofreire.core.repository.UserRepository;
import io.renatofreire.core.repository.VerificationTokenRepository;
import io.renatofreire.core.security.JWTService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RabbitService rabbitService;



    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository, RoleRepository roleRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager, RabbitService rabbitService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.rabbitService = rabbitService;
    }


    public void register(final RegistrationRequest registrationRequest) {

        final User user = userRepository.findByEmail(registrationRequest.email()).orElse(null);
        if (user != null) {
            throw new EntityExistsException("Email already taken");
        }

        final User newUser = userRepository.saveAndFlush(User.builder()
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .email(registrationRequest.email())
                .birthDate(registrationRequest.birthDate())
                .password(passwordEncoder.encode(registrationRequest.password()))
                .roles(Set.of(roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new EntityNotFoundException("Role not found"))))
                .build());

        final VerificationToken verificationToken = verificationTokenRepository.saveAndFlush(
                VerificationToken.builder()
                        .token(UUID.randomUUID().toString().replace("-", ""))
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(30))
                        .user(newUser)
                        .build()
        );

        rabbitService.sendMessage(new RegisteredUserMessageDto(registrationRequest.firstName()  + " " + registrationRequest.lastName(),
                registrationRequest.email(), verificationToken.getToken()));
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest authenticateRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticateRequest.email(),
                        authenticateRequest.password()
                )
        );

        final boolean userVerifies = userRepository.isUserVerified(authenticateRequest.email());
        if(!userVerifies) {
            throw new EntityNotFoundException("User is not verified");
        }

        final User user = userRepository.findByEmail(authenticateRequest.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        revokeAllUserTokens(user);
        final String jwtToken = saveJWTToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;

        if(authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")){
            throw new InvalidFieldException();
        }

        refreshToken = authenticationHeader.substring(7);

        final String userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail == null) {
            throw new InvalidFieldException();
        }

        final User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(EntityNotFoundException::new);

        if(!jwtService.isTokenValid(refreshToken, user)){
            throw new InvalidFieldException();
        }

        revokeAllUserTokens(user);
        final String accessToken = saveJWTToken(user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public boolean verifyToken(final String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Verification token not found"));
        if (verificationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyValidatedException("email already confirmed");
        }

        LocalDateTime expiredAt = verificationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenAlreadyExpiredException("token expired");
        }
        verificationToken.setConfirmedAt(LocalDateTime.now());
        verificationTokenRepository.saveAndFlush(verificationToken);

        Optional<User> isUser = userRepository.findById(verificationToken.getUser().getId());
        if (isUser.isPresent()) {
            User user = isUser.get();
            user.setEmailValidated(true);
            userRepository.saveAndFlush(user);
        }

        logger.info("New email verified");
        return true;
    }

    private String saveJWTToken(final User newUser) {
        final User savedUser = userRepository.save(newUser);

        final String jwtToken = jwtService.generateJWTToken(newUser);

        Token token = new Token(savedUser,  false,false, TokenType.BEARER, jwtToken);
        tokenRepository.save(token);
        return jwtToken;
    }

    private void revokeAllUserTokens(final User newUser) {
        List<Token> allValidTokens = tokenRepository.findActiveTokensByUserId(newUser.getId());
        if(allValidTokens.isEmpty()){
            return;
        }

        allValidTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(allValidTokens);
    }

}