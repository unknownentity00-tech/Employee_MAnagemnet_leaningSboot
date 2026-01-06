package com.Learning.Employee_Management.service;

import com.Learning.Employee_Management.ResourceNotFoundException;
import com.Learning.Employee_Management.entity.User;
import com.Learning.Employee_Management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Logic: Used by AuthService during manual registration
    public User registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Logic: Used by OAuth2SuccessHandler to find or auto-create a Google/GitHub user
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }






}
