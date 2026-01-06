package com.Learning.Employee_Management.controller;

import com.Learning.Employee_Management.Dto.LoginRequest;
import com.Learning.Employee_Management.Dto.LoginResponse;
import com.Learning.Employee_Management.entity.User;
import com.Learning.Employee_Management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") // Defines the "door" to the login module
@RequiredArgsConstructor // Injects the AuthService bean
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login") // Matches Step 1: Login Request in your diagrams
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // Sends the request to the AuthService pipeline
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
//        return ResponseEntity.ok(authService.login(loginRequest));
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }
}