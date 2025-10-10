package com.jian.community.application.service;

import com.jian.community.application.util.PasswordEncoder;
import com.jian.community.domain.constant.ErrorCode;
import com.jian.community.domain.exception.BadRequestException;
import com.jian.community.domain.exception.NotFoundException;
import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.UserRepository;
import com.jian.community.presentation.dto.AvailabilityResponse;
import com.jian.community.presentation.dto.CreateUserRequest;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public void createUser(CreateUserRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new BadRequestException(
                            ErrorCode.USER_ALREADY_EXISTS,
                            "이미 사용 중인 이메일입니다."
                    );
                });

        userRepository.findByNickname(request.nickname())
                .ifPresent(user -> {
                    throw new BadRequestException(
                            ErrorCode.USER_ALREADY_EXISTS,
                            "이미 사용 중인 닉네임입니다."
                    );
                });

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.of(
                request.email(),
                encodedPassword,
                request.nickname(),
                request.profileImageUrl()
        );
        userRepository.save(user);
    }

    public AvailabilityResponse validateEmail(String email) {
        boolean isAvailable = userRepository.existsByEmail(email);
        return new AvailabilityResponse(isAvailable);
    }

    public AvailabilityResponse validateNickname(String nickname) {
        boolean isAvailable = userRepository.existsByNickname(nickname);
        return new AvailabilityResponse(isAvailable);
    }
}
