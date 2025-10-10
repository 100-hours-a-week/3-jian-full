package com.jian.community.presentation.controller;

import com.jian.community.application.service.UserService;
import com.jian.community.presentation.dto.CreateUserRequest;
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
}
