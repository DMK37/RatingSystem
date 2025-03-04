package org.example.ratingsystem.config;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.config.auth.SecurityFilter;
import org.example.ratingsystem.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {
    private final SecurityFilter securityFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/*/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/*/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/comments/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/comments/*").hasRole(Role.USER.name())
                        .requestMatchers(HttpMethod.PUT, "/users/comments/*").hasRole(Role.USER.name())
                        .requestMatchers("/admin/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
