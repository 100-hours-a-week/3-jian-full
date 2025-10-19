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
    private final Map<String, UserSession> sessionIdIndex = new ConcurrentHashMap<>();
    private final Map<String, Object> locks  = new ConcurrentHashMap<>();

    public UserSessionInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public UserSession save(UserSession userSession) {
        UserSession saved = delegate.save(userSession);
        sessionIdIndex.put(saved.getSessionId(), saved);
        return saved;
    }

    public Optional<UserSession> findBySessionId(String sessionId) {
        UserSession userSession = sessionIdIndex.get(sessionId);
        return Optional.ofNullable(userSession);
    }

    public void deleteBySessionId(String sessionId) {
        UserSession userSession = sessionIdIndex.get(sessionId);
        if (userSession == null) return;

        Object lock = lockFor(sessionId);
        synchronized (lock) {
            sessionIdIndex.remove(sessionId);
            delegate.deleteById(userSession.getId());
        }
    }

    private Object lockFor(String sessionId) {
        return locks.computeIfAbsent(sessionId, k -> new Object());
    }
}
