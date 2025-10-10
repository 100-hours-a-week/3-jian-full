package com.jian.community.application.service;

import com.jian.community.application.util.PasswordEncoder;
import com.jian.community.domain.constant.ErrorCode;
import com.jian.community.domain.exception.BadRequestException;
import com.jian.community.domain.exception.NotFoundException;
import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(User user){}

    public Long authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.USER_NOT_EXISTS,
                        "사용자를 찾을 수 없습니다."
                ));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(
                    ErrorCode.INVALID_CREDENTIALS,
                    "인증 정보가 올바르지 않습니다."
            );
        }
        return user.getId();
    }
}
