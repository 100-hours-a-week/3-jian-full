package com.jian.community.domain.repository;

import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    void deleteById(Long userId);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    default User findByIdOrThrow(Long userId) {
        return findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_EXISTS));
    }
}
