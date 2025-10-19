package com.jian.community.infrastructure.memory;

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
    private final Map<Long, List<PostLike>> postIdIndex = new ConcurrentHashMap<>();
    private final Map<Long, List<PostLike>> userIdIndex = new ConcurrentHashMap<>();
    private final Map<PostUserKey, PostLike> postIdUserIdIndex = new ConcurrentHashMap<>();
    private final Map<Long, Object> locks = new ConcurrentHashMap<>();

    public PostLikeInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public PostLike save(PostLike postLike) {
        if (postLike.getId() != null) {
            return delegate.save(postLike);
        }
        PostLike saved = delegate.save(postLike);

        Object lock = lockFor(saved.getId());
        synchronized (lock) {
            postIdIndex
                    .computeIfAbsent(saved.getPostId(), k -> new CopyOnWriteArrayList<>())
                    .add(saved);
            userIdIndex
                    .computeIfAbsent(saved.getUserId(), k -> new CopyOnWriteArrayList<>())
                    .add(saved);
            postIdUserIdIndex.put(new PostUserKey(saved.getPostId(), saved.getUserId()), saved);
            return saved;
        }
    }

    @Override
    public Optional<PostLike> findById(Long postLikeId) {
        return delegate.findById(postLikeId);
    }

    @Override
    public List<PostLike> findByPostId(Long postId) {
        List<PostLike> postLikes = postIdIndex.get(postId);
        if (postLikes == null) return Collections.emptyList();
        return List.copyOf(postLikes); // 배열 원본 보호
    }

    public void deleteById(Long postLikeId) {
        Object lock = lockFor(postLikeId);
        synchronized (lock) {
            removeIndices(postLikeId);
            delegate.deleteById(postLikeId);
        }
    }

    @Override
    public void deleteByPostIdAndUserId(Long postId, Long userId) {
        PostLike target = postIdUserIdIndex.get(new PostUserKey(postId, userId));
        if (target == null) return;
        deleteById(target.getId());
    }

    private void removeIndices(Long postLikeId) {
        PostLike target = findById(postLikeId).orElse(null);
        if (target == null) return;

        postIdIndex.computeIfPresent(
                target.getPostId(),
                (key, list) -> {
                    list.remove(target);
                    return list;
                }
        );
        userIdIndex.computeIfPresent(
                target.getUserId(),
                (key, list) -> {
                    list.remove(target);
                    return list;
                }
        );
        postIdUserIdIndex.remove(new PostUserKey(target.getPostId(), target.getUserId()));
    }

    private Object lockFor(Long postLikeId) {
        return locks.computeIfAbsent(postLikeId, k -> new Object());
    }

    private record PostUserKey(Long postId, Long userId) {
    }
}
