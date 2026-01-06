package com.Learning.Employee_Management.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.core.userdetails.User.withUsername;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig  {

//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain  securityFilterChain(HttpSecurity httpSecurity ) throws Exception{
//        httpSecurity
    // 1. DISABLE CSRF:
    // We disable this because CSRF protection is designed for sessions/cookies.
    // Since we are using JWT (stateless), we don't need it.
//                .csrf(csrfConfig->csrfConfig.disable())
    // 2. SET STATELESS SESSION:
    // This tells Spring Boot: "Do not save user data in a session on the server".
    // Every request must be independent and carry its own "ID" (the JWT).
//                .sessionManagement(sessionconfig ->
//                        sessionconfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // 3. DEFINE URL PERMISSIONS:
//                .authorizeHttpRequests(auth->auth
    // Allow everyone to access URLs starting with /auth/ (Login/Register).
    // They don't need a token to get here because they are trying to GET one.
//                        .requestMatchers("/api/employees/**")
    // Only allow users with the "ADMIN" role to touch employee data.
    // Spring will look for "ROLE_ADMIN" in your database/token.
//                        .permitAll()
    // Any other URL not mentioned above (like /api/profile)
    // requires the user to be logged in with a valid token.
//                        .anyRequest().authenticated()
//                )
//                .formLogin(Customizer.withDefaults());
//        return httpSecurity.build();
//    }
//   @Bean
//   public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//@Bean
//    UserDetailsService userDetailsService(){
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("pass"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails employee = User.builder()
//                .username("employee")
//                .password(passwordEncoder().encode("pass"))
//                .roles("EMPLOYEE")
//                .build();
//
//
//        return new InMemoryUserDetailsManager(admin  , employee
//        );
//    }
//
//


    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/employees/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // Logic: Guard checks the JWT Dish for every internal request
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Logic: The Door for Google/GitHub (Step 1 in your diagram)
                .oauth2Login(oauth2Config -> oauth2Config
                        .failureUrl("/login?error=true")
                        .successHandler(oAuth2SuccessHandler) // Step: "triggers OAuth2SuccessHandler"
                );

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}