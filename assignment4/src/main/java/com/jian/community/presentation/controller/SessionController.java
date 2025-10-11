package com.jian.community.presentation.controller;

import com.jian.community.application.service.SessionManager;
import com.jian.community.application.service.UserService;
import com.jian.community.presentation.dto.CreateSessionRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@AllArgsConstructor
public class SessionController {

    private final UserService userService;
    private final SessionManager sessionManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createSession(@RequestBody CreateSessionRequest request, HttpServletResponse httpResponse) {
        Long userId = userService.authenticate(request.email(), request.password());
        sessionManager.createSession(userId, httpResponse);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void expireSession(HttpServletRequest httpRequest){
        sessionManager.expireSession(httpRequest);
    }
}
