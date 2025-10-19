package com.jian.community.infrastructure.memory;

import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.Comment;
import com.jian.community.domain.repository.CommentRepository;
import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Repository
@Primary
public class CommentInMemoryRepository implements CommentRepository {

    private final InMemoryRepository<Comment> delegate;
    private final Map<Long, ConcurrentSkipListMap<CommentCreatedAtKey, Comment>> postIdIndex = new ConcurrentHashMap<>();
    private final Map<Long, Object> locks = new ConcurrentHashMap<>();

    public CommentInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() != null) {
            return delegate.save(comment);
        }
        Comment saved = delegate.save(comment);
        postIdIndex
                .computeIfAbsent(saved.getPostId(), k -> new ConcurrentSkipListMap<>())
                .putIfAbsent(new CommentCreatedAtKey(saved.getId(), saved.getCreatedAt()), saved);
        return saved;
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return delegate.findById(commentId);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        ConcurrentSkipListMap<CommentCreatedAtKey, Comment> createdAtIndex = postIdIndex.get(postId);
        if (createdAtIndex == null) return Collections.emptyList();
        return List.copyOf(createdAtIndex.values()); // 원본 배열 보호
    }

    @Override
    public CursorPage<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId, LocalDateTime cursor, int pageSize) {
        ConcurrentSkipListMap<CommentCreatedAtKey, Comment> createdAtIndex =
                postIdIndex.getOrDefault(postId, new ConcurrentSkipListMap<>());

        NavigableMap<CommentCreatedAtKey, Comment> window = (cursor == null)
                        ? createdAtIndex.descendingMap()
                        : createdAtIndex.headMap(new CommentCreatedAtKey(null, cursor), false).descendingMap();

        List<Comment> paged = window.values().stream()
                .limit(pageSize + 1)
                .toList();

        boolean hasNext = paged.size() > pageSize;

        return new CursorPage<>(paged.subList(0, Math.min(paged.size(), pageSize)), hasNext);
    }

    @Override
    public void deleteById(Long commentId) {
        Object lock = lockFor(commentId);
        synchronized (lock) {
            removeIndices(commentId);
            delegate.deleteById(commentId);
        }
    }

    private void removeIndices(Long commentId) {
        Comment target = findById(commentId).orElse(null);
        if (target == null) return;

        postIdIndex.computeIfPresent(
                target.getPostId(),
                (k, idx) -> {
                    idx.remove(new CommentCreatedAtKey(target.getId(), target.getCreatedAt()));
                    return idx;
                });
    }

    private Object lockFor(Long commentId) {
        return locks.computeIfAbsent(commentId, k -> new Object());
    }

    private record CommentCreatedAtKey(Long commentId, LocalDateTime createdAt) implements Comparable<CommentCreatedAtKey> {
        @Override
        public int compareTo(CommentCreatedAtKey o) {
            int cmp = createdAt.compareTo(o.createdAt);
            return (cmp == 0) ? commentId.compareTo(o.commentId) : cmp;
        }
    }
}
