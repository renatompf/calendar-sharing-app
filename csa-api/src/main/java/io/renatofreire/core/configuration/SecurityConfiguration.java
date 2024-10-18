package io.renatofreire.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    public static final String AUTH_PATH = "/auth/**";

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;

    public SecurityConfiguration(JWTAuthenticationFilter jwtAuthFilter, LogoutHandler logoutHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new SessionFixationProtectionStrategy();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(AUTH_PATH).permitAll();
                    auth.requestMatchers("/calendar-sharing/token-validation").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> {
                    logout.logoutUrl("/auth/logout");
                    logout.addLogoutHandler(logoutHandler);
                    logout.logoutSuccessHandler(
                            ((request, response, authentication) -> SecurityContextHolder.clearContext())
                    );
                });

        return httpSecurity.build();
    }
}
