package com.hrworkflow.identityservice.utils;

import com.hrworkflow.identityservice.model.User;
import com.hrworkflow.identityservice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");

        if (userId != null) {
            System.out.println("X-User-Id: " + userId);

            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                System.out.println("User found: " + user.getUsername() + ", Role: " + user.getRole());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authentication set for user: " + user.getUsername());
            } else {
                System.out.println("User not found in database!");
            }
        } else {
            System.out.println("No X-User-Id header found");
        }

        filterChain.doFilter(request, response);
    }
}