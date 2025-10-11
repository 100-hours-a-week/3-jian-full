package com.jian.community.presentation.controller;

import com.jian.community.application.service.UserService;
import com.jian.community.presentation.dto.AvailabilityResponse;
import com.jian.community.presentation.dto.CreateUserRequest;
import com.jian.community.presentation.dto.EmailAvailabilityRequest;
import com.jian.community.presentation.dto.NicknameAvailabilityRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
    }

    @GetMapping("/emails/availability")
    @ResponseStatus(HttpStatus.OK)
    public AvailabilityResponse validateEmail(@Valid @ModelAttribute EmailAvailabilityRequest request) {
        return userService.validateEmail(request.email());
    }

    @GetMapping("/nicknames/availability")
    @ResponseStatus(HttpStatus.OK)
    public AvailabilityResponse validateNickname(@Valid @ModelAttribute NicknameAvailabilityRequest request) {
        return userService.validateNickname(request.nickname());
    }
}
