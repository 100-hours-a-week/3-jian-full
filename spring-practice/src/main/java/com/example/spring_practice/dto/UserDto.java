package com.example.spring_practice.dto;

public class UserDto {

    String username;
    String email;

    public String getUsername() { return username; }
    public String getEmail() { return email; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }

    public String toString() {
        return "UserDto{username='" + username + "', email='" + email + "'}";
    }
}
