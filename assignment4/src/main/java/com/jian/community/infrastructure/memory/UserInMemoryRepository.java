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
    private final Map<String, Long> emailIndex  = new ConcurrentHashMap<>();

    public UserInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public User save(User user) {
        User saved = delegate.save(user);
        emailIndex.put(saved.getEmail(), saved.getId());
        return saved;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return delegate.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Long id = emailIndex.get(email);
        if (id == null) return Optional.empty();
        return delegate.findById(id);
    }

    @Override
    public void deleteById(Long userId) {
        delegate.deleteById(userId);
    }

    @Override
    public boolean existsById(Long userId) {
        return delegate.existsById(userId);
    }
}
