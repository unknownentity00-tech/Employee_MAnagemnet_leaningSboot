package com.Learning.Employee_Management.service;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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
//                .csrf(csrfConfig->csrfConfig.disable())
//                .sessionManagement(sessionconfig ->
//                        sessionconfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth->auth
//                        .requestMatchers("/api/employees/**")
//                        .permitAll()
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

@Autowired
    private final JwtAuthFilter jwtAuthFilter; // Logic: Bring the Guard to the building

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Logic: JWT doesn't need CSRF
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Logic: No Cookies
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Logic: Public login door
                        .requestMatchers("/api/employees/**").hasRole("ADMIN") // Logic: Protected Module
                        .anyRequest().authenticated()
                )
                // Logic: Tell the Guard to check tokens BEFORE checking passwords
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
