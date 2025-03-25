package com.hrworkflow.workflowservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/applications/apply/**").hasRole("CANDIDATE")
                        .requestMatchers(HttpMethod.GET, "/applications/myApplications").hasRole("CANDIDATE")
                        .requestMatchers(HttpMethod.GET, "/applications/**").hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.PUT, "/applications/**").hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.DELETE, "/applications/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/applications/myApplications/**").hasRole("CANDIDATE")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new UserDetailsFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
