package com.jian.community.presentation.controller;

import com.jian.community.application.service.SessionManager;
import com.jian.community.application.service.UserService;
import com.jian.community.presentation.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private SessionManager sessionManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse getMyInfo(HttpServletRequest request) {
        Long userId = sessionManager.getSession(request).getUserId();
        return userService.getUserInfo(userId);
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
