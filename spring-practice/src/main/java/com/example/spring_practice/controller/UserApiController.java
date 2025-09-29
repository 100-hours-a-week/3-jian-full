package com.example.spring_practice.controller;

import com.example.spring_practice.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    @PostMapping("/api/user")
    public String createUser(@RequestBody UserDto userDto) {
        System.out.println("Received data: " + userDto.toString());

        return "User created successfully: " + userDto.getUsername();
    }
}
