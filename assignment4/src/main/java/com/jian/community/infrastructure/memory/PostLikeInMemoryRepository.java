package com.jian.community.infrastructure.memory;

import com.jian.community.domain.model.PostLike;
import com.jian.community.domain.repository.PostLikeRepository;
import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class PostLikeInMemoryRepository implements PostLikeRepository {

    private final InMemoryRepository<PostLike> delegate;
    private final Map<Long, List<PostLike>> postIdIndex = new ConcurrentHashMap<>();
    private final Map<Long, List<PostLike>> userIdIndex = new ConcurrentHashMap<>();
    private final Map<PostUserKey, PostLike> postIdUserIdIndex = new ConcurrentHashMap<>();

    public PostLikeInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public PostLike save(PostLike postLike) {
        PostLike saved = delegate.save(postLike);
        postIdIndex.compute(saved.getPostId(), (k, list) ->
                Optional.ofNullable(list)
                        .map(l -> { l.add(saved); return l; })
                        .orElseGet(() -> new ArrayList<>(List.of(saved)))
        );
        userIdIndex.compute(saved.getUserId(), (k, list) ->
                Optional.ofNullable(list)
                        .map(l -> { l.add(saved); return l; })
                        .orElseGet(() -> new ArrayList<>(List.of(saved)))
        );
        postIdUserIdIndex.put(new PostUserKey(saved.getPostId(), saved.getUserId()), postLike);
        return saved;
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
        removeIndices(postLikeId);
        delegate.deleteById(postLikeId);
    }

    @Override
    public void deleteByPostIdAndUserId(Long postId, Long userId) {
        PostLike target = postIdUserIdIndex.get(new PostUserKey(postId, userId));
        if (target == null) return;
        deleteById(target.getId());
    }

    private void removeIndices(Long postLikeId) {
        Optional<PostLike> target = findById(postLikeId);
        if (target.isEmpty()) return;

        postIdIndex.computeIfPresent(
                target.get().getPostId(),
                (key, list) -> {
                    list.remove(target.get());
                    return list;
                }
        );
        userIdIndex.computeIfPresent(
                target.get().getUserId(),
                (key, list) -> {
                    list.remove(target.get());
                    return list;
                }
        );
        postIdUserIdIndex.remove(new PostUserKey(target.get().getPostId(), target.get().getUserId()));
    }

    private record PostUserKey(Long postId, Long userId) {
    }
}
