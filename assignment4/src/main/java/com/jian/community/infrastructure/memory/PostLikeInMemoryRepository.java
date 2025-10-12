package com.jian.community.infrastructure.memory;

import com.jian.community.domain.model.MinimalEntity;
import com.jian.community.domain.model.PostLike;
import com.jian.community.domain.repository.PostLikeRepository;
import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Primary
public class PostLikeInMemoryRepository implements PostLikeRepository {

    private final InMemoryRepository<PostLike> delegate;
    private final Map<Long, List<Long>> postIdIndex = new ConcurrentHashMap<>();
    private final Map<Long, List<Long>> userIdIndex = new ConcurrentHashMap<>();

    public PostLikeInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public PostLike save(PostLike postLike) {
        PostLike saved = delegate.save(postLike);
        postIdIndex
                .computeIfAbsent(saved.getPostId(), key -> new CopyOnWriteArrayList<>())
                .add(saved.getId());

        userIdIndex
                .computeIfAbsent(saved.getUserId(), key -> new CopyOnWriteArrayList<>())
                .add(saved.getId());
        return saved;
    }

    @Override
    public Optional<PostLike> findById(Long postLikeId) {
        return delegate.findById(postLikeId);
    }

    @Override
    public List<PostLike> findByPostId(Long postId) {
        List<Long> postLikeIds = postIdIndex.get(postId);
        if (postLikeIds == null || postLikeIds.isEmpty()) return Collections.emptyList();

        return postLikeIds.stream()
                .map(this::findById)
                .flatMap(Optional::stream)
                .toList();
    }

    public void deleteById(Long postLikeId) {
        delegate.deleteById(postLikeId);
    }

    @Override
    public void deleteByPostIdAndUserId(Long postId, Long userId) {
        List<Long> postLikeIds = postIdIndex.get(postId);
        if (postLikeIds == null || postLikeIds.isEmpty()) return;

        List<PostLike> existingPostLikes = postLikeIds.stream()
                .map(this::findById)
                .flatMap(Optional::stream)
                .toList();

        existingPostLikes.stream()
                .filter(postLike -> postLike.getUserId().equals(userId))
                .map(MinimalEntity::getId)
                .forEach(this::deleteById);
    }
}
