package com.Learning.Employee_Management.service;

import com.Learning.Employee_Management.Dto.LoginRequest;
import com.Learning.Employee_Management.Dto.LoginResponse;
import com.Learning.Employee_Management.entity.User;
import com.Learning.Employee_Management.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
@Service
@AllArgsConstructor
public class AuthService {
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        // 1. Send credentials to the Authentication Manager (The Receptionist)
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//
//
// 1. Authenticate against Database (via UserDetailsService)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())

        );

        // 2. Retrieve the User Entity from the successful authentication
        User user = (User) authentication.getPrincipal();

        // 3. Prepare the "JWT Dish" using your secret salt from application.properties
//        String token = jwtUtils.generateToken(user);
        String  accesstoken  = jwtUtils.generateToken( user);
        String  refreshtoken = jwtUtils.generateRefreshToken(user);
        // 4. Return the response. Ensure types match your LoginResponse constructor!
        // If your DTO expects (String, Long), use user.getId()
//        return new LoginResponse(token,
//                user.getUsername(), // Fixed: Long converted to String
//                user.getRole());

        return new LoginResponse(accesstoken , refreshtoken);
    }

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Logic: Hash the password!
        userRepository.save(user);
        return "User Registered Successfully";
    }
}