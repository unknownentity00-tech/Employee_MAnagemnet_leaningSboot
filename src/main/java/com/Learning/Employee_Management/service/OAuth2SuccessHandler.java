package com.Learning.Employee_Management.service;

import com.Learning.Employee_Management.entity.User;
import com.Learning.Employee_Management.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component // Logic: Makes this class discoverable by WebSecurityConfig
@RequiredArgsConstructor // Logic: Injects the final repository and utils
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1. Logic: Extract Identity from Google/GitHub
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // 2. Logic: Database Sync using the new findByEmail method
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(email) // or name from oAuth2User.getAttribute("name")
                        .email(email)
                        .password("")
                        .role("ROLE_USER")
                        .build()));

        // 3. Cook the "Dish" (Internal JWT)
        String accessToken = jwtUtils.generateToken(user);

        // 4. Logic: Delivery via Redirect
        // This matches the "return the response" step in your diagram
        String targetUrl = "http://localhost:3000/oauth2/callback?token=" + accessToken;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}