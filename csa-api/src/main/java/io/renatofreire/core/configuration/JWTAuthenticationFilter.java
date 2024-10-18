package io.renatofreire.core.configuration;

import io.renatofreire.core.model.User;
import io.renatofreire.core.repository.TokenRepository;
import io.renatofreire.core.repository.UserRepository;
import io.renatofreire.core.security.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;


    public JWTAuthenticationFilter(JWTService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authenticationHeader = request.getHeader(AUTHORIZATION);
        final String jwtToken;


        if (authenticationHeader == null || !authenticationHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authenticationHeader.substring(7);

        final String userEmail = jwtService.extractUsername(jwtToken);
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            boolean isUserVerified = userRepository.isUserVerified(userEmail);
            boolean isTokenValid = tokenRepository.findByToken(jwtToken)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if(jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid && isUserVerified){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}