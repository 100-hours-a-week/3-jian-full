package com.jian.community.application.service;

import com.jian.community.application.exception.ErrorMessage;
import com.jian.community.application.util.PasswordEncoder;
import com.jian.community.application.exception.ErrorCode;
import com.jian.community.application.exception.BadRequestException;
import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.UserRepository;
import com.jian.community.presentation.dto.*;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new BadRequestException(
                    ErrorCode.INVALID_CREDENTIALS,
                    ErrorMessage.INVALID_CREDENTIALS
            );
        }

        return user.get().getId();
    }

    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail((request.email()))) {
            throw new BadRequestException(
                    ErrorCode.USER_ALREADY_EXISTS,
                    ErrorMessage.EMAIL_ALREADY_EXISTS
            );
        }

        if (userRepository.existsByNickname(request.nickname())) {
            throw new BadRequestException(
                    ErrorCode.USER_ALREADY_EXISTS,
                    ErrorMessage.NICKNAME_ALREADY_EXISTS
            );
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.of(
                request.email(),
                encodedPassword,
                request.nickname(),
                request.profileImageUrl()
        );
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findByIdOrThrow(userId);
        return new UserInfoResponse(user.getEmail(), user.getNickname(), user.getProfileImageUrl());
    }

    public UserInfoResponse updateUserInfo(Long userId, UpdateUserRequest request) {
        User user = userRepository.findByIdOrThrow(userId);
        user.update(request.nickname());
        userRepository.save(user);

        return new UserInfoResponse(user.getEmail(), user.getNickname(), user.getProfileImageUrl());
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findByIdOrThrow(userId);

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new BadRequestException(
                    ErrorCode.INVALID_CREDENTIALS,
                    ErrorMessage.INVALID_CREDENTIALS
            );
        }

        String encodedPassword = passwordEncoder.encode(request.newPassword());
        user.changePassword(encodedPassword);
        userRepository.save(user);
    }

    public AvailabilityResponse validateEmail(String email) {
        boolean isAvailable = !userRepository.existsByEmail(email);
        return new AvailabilityResponse(isAvailable);
    }

    public AvailabilityResponse validateNickname(String nickname) {
        boolean isAvailable = !userRepository.existsByNickname(nickname);
        return new AvailabilityResponse(isAvailable);
    }
}
