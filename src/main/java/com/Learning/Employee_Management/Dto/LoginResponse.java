package com.Learning.Employee_Management.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String role;
    private String accessToken;
    private String refreshToken;

    public LoginResponse(String accesstoken, String refreshtoken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getaccessToken() {
        return accessToken;
    }
    public String getrefreshtoken() {
        return refreshToken;
    }
}