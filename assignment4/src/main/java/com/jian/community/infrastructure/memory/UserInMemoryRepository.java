package com.jian.community.infrastructure.memory;

import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.UserRepository;
import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class UserInMemoryRepository implements UserRepository {

    private final InMemoryRepository<User> delegate;
    private final Map<String, User> emailIndex  = new ConcurrentHashMap<>();
    private final Map<String, User> nicknameIndex = new ConcurrentHashMap<>();

    public UserInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public User save(User user) {
        User saved = delegate.save(user);
        emailIndex.put(saved.getEmail(), saved);
        nicknameIndex.put(saved.getNickname(), saved);
        return saved;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return delegate.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user = emailIndex.get(email);
        return Optional.ofNullable(user);
    }

    @Override
    public void deleteById(Long userId) {
        delegate.deleteById(userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        User user = emailIndex.get(email);
        return user != null;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        User user = nicknameIndex.get(nickname);
        return user != null;
    }
}
