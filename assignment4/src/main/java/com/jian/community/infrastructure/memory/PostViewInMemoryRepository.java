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
    private final Map<Long, Long> postIdIndex = new ConcurrentHashMap<>();


    public PostViewInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public PostView save(PostView postView) {
        PostView saved = delegate.save(postView);
        postIdIndex.put(saved.getPostId(), saved.getId());
        return saved;
    }

    @Override
    public Optional<PostView> findByPostId(Long postId) {
        Long id = postIdIndex.get(postId);
        if (id == null) return Optional.empty();
        return delegate.findById(id);
    }

    @Override
    public void deleteByPostId(Long postId) {
        delegate.deleteById(postId);
    }
}
