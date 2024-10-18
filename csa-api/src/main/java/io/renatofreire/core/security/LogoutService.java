package io.renatofreire.core.security;

import io.renatofreire.core.model.Token;
import io.renatofreire.core.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    public LogoutService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authenticationHeader = request.getHeader("Authorization");
        final String jwtToken;

        if(authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")){
            return;
        }

        jwtToken = authenticationHeader.substring(7);
        Token storedToken = tokenRepository.findByToken(jwtToken).orElse(null);

        if(storedToken != null){
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            tokenRepository.save(storedToken);
        }
    }
}