package com.jian.community.presentation.controller;

import com.jian.community.application.service.UserService;
import com.jian.community.presentation.dto.*;
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
    public void signUp(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse getMyInfo(@RequestAttribute Long userId) {
        return userService.getUserInfo(userId);
    }

    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse updateMyInfo(
            @Valid @RequestBody UpdateUserRequest request,
            @RequestAttribute Long userId
    ) {
        return userService.updateUserInfo(userId, request);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyAccount(@RequestAttribute Long userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeMyPassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestAttribute Long userId
    ) {
        userService.changePassword(userId, request);
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
