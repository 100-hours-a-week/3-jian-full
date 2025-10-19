package com.jian.community.presentation.controller;

import com.jian.community.application.service.UserService;
import com.jian.community.presentation.dto.*;
import com.jian.community.presentation.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
    }

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse getMyInfo(@RequestAttribute Long userId) {
        return userService.getUserInfo(userId);
    }

    @Operation(summary = "내 정보 수정", description = "로그인한 사용자의 정보를 수정합니다.")
    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse updateMyInfo(
            @Valid @RequestBody UpdateUserRequest request,
            @RequestAttribute Long userId
    ) {
        return userService.updateUserInfo(userId, request);
    }

    @Operation(summary = "회원 탈퇴", description = "로그인한 사용자의 회원 정보를 영구 삭제합니다.")
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyAccount(@RequestAttribute Long userId) {
        userService.deleteUser(userId);
    }

    @Operation(summary = "비밀번호 변경")
    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeMyPassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestAttribute Long userId
    ) {
        userService.changePassword(userId, request);
    }

    @Operation(summary = "이메일 중복 검사", description = "회원 가입시 해당 이메일 사용 가능 여부를 검사합니다.")
    @GetMapping("/emails/availability")
    @ResponseStatus(HttpStatus.OK)
    public AvailabilityResponse validateEmail(@Valid @ModelAttribute EmailAvailabilityRequest request) {
        return userService.validateEmail(request.email());
    }

    @Operation(summary = "닉네임 중복 검사", description = "회원 가입시 해당 닉네임 사용 가능 여부를 검사합니다.")
    @GetMapping("/nicknames/availability")
    @ResponseStatus(HttpStatus.OK)
    public AvailabilityResponse validateNickname(@Valid @ModelAttribute NicknameAvailabilityRequest request) {
        return userService.validateNickname(request.nickname());
    }
}
