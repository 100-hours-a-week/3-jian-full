package com.jian.community.infrastructure.memory;

import com.jian.community.domain.model.PostView;
import com.jian.community.domain.repository.PostViewRepository;
import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class PostViewInMemoryRepository implements PostViewRepository {

    private final InMemoryRepository<PostView> delegate;
    private final Map<Long, PostView> postIdIndex = new ConcurrentHashMap<>();
    private final Map<Long, Object> locks = new ConcurrentHashMap<>();

    public PostViewInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public PostView save(PostView postView) {
        PostView saved = delegate.save(postView);
        postIdIndex.put(saved.getPostId(), saved);
        return saved;
    }

    @Override
    public Optional<PostView> findByPostId(Long postId) {
        PostView postView = postIdIndex.get(postId);
        return Optional.ofNullable(postView);
    }

    @Override
    public void deleteByPostId(Long postId) {
        Object lock = lockFor(postId);
        synchronized (lock) {
            removeIndices(postId);
            delegate.deleteById(postId);
        }
    }

    private void removeIndices(Long postViewId) {
        PostView target = delegate.findById(postViewId).orElse(null);
        if (target == null) return;
        postIdIndex.remove(target.getPostId());
    }

    private Object lockFor(Long postViewId) {
        return locks.computeIfAbsent(postViewId, k -> new Object());
    }
}
