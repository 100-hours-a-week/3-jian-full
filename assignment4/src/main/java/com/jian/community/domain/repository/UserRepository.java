package com.jian.community.domain.repository;

import com.jian.community.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    public User save(User user);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    void deleteById(Long userId);

    boolean existsById(Long userId);

    Optional<User> findByNickname(String nickname);
}
