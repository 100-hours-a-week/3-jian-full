package com.jian.community.infrastructure.memory;

import com.jian.community.domain.model.UserSession;
import com.jian.community.domain.repository.UserSessionRepository;
import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class UserSessionInMemoryRepository implements UserSessionRepository {

    private final InMemoryRepository<UserSession> delegate;
    private final Map<String, Long> sessionIdIndex = new ConcurrentHashMap<>();

    public UserSessionInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public UserSession save(UserSession userSession) {
        UserSession saved = delegate.save(userSession);
        sessionIdIndex.put(saved.getSessionId(), saved.getId());
        return saved;
    }

    public Optional<UserSession> findBySessionId(String sessionId) {
        Long id = sessionIdIndex.get(sessionId);
        if (id == null) return Optional.empty();
        return delegate.findById(id);
    }

    public void deleteBySessionId(String sessionId) {
        Long id = sessionIdIndex.get(sessionId);
        if (id == null) return;
        delegate.deleteById(id);
    }

    public boolean existsBySessionId(String sessionId) {
        Long id = sessionIdIndex.get(sessionId);
        if (id == null) return false;
        return delegate.existsById(id);
    }
}
