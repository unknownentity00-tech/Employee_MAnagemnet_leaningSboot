package com.Learning.Employee_Management.service;

import com.Learning.Employee_Management.entity.User;
import com.Learning.Employee_Management.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtUtils authUtil; // Logic: Inject the "Scientist" to verify the dish

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // Logic: If no Bearer token, move to next filter (e.g., permitAll endpoints)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. Structural Extraction
        String token = authHeader.substring(7);

        // 2 & 3. Cryptographic Re-calculation (using your Salt from properties)
        String username = authUtil.getUsernameFromToken(token);



        // 4. Integration with Protected Modules
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Logic: Fetch user entity to confirm they still exist in your DB
            User user = userRepository.findByUsername(username).orElse(null);

            if (user != null) {
                // Logic: Create the "Verified Badge" (Token)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );

                // Logic: Set the Security Context (Step 6 in your diagram)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}