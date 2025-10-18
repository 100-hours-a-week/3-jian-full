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
    private final Map<Long, ConcurrentSkipListMap<LocalDateTime, Comment>> postIdIndex = new ConcurrentHashMap<>();

    public CommentInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public Comment save(Comment comment) {
        Comment saved = delegate.save(comment);
        postIdIndex
                .computeIfAbsent(saved.getPostId(), k -> new ConcurrentSkipListMap<>())
                .putIfAbsent(saved.getCreatedAt(), saved);
        return saved;
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return delegate.findById(commentId);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        ConcurrentSkipListMap<LocalDateTime, Comment> createdAtIndex = postIdIndex.get(postId);
        if (createdAtIndex == null) return Collections.emptyList();
        return List.copyOf(createdAtIndex.values()); // 원본 배열 보호
    }

    @Override
    public CursorPage<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId, LocalDateTime cursor, int pageSize) {
        ConcurrentSkipListMap<LocalDateTime, Comment> createdAtIndex =
                postIdIndex.getOrDefault(postId, new ConcurrentSkipListMap<>());

        NavigableMap<LocalDateTime, Comment> window = (cursor == null)
                        ? createdAtIndex.descendingMap()
                        : createdAtIndex.headMap(cursor, false).descendingMap();

        List<Comment> paged = window.values().stream()
                .limit(pageSize + 1)
                .toList();

        boolean hasNext = paged.size() > pageSize;

        return new CursorPage<>(paged.subList(0, Math.min(paged.size(), pageSize)), hasNext);
    }

    @Override
    public void deleteById(Long commentId) {
        removeIndices(commentId);
        delegate.deleteById(commentId);
    }

    private void removeIndices(Long commentId) {
        Optional<Comment> target = findById(commentId);
        if (target.isEmpty()) return;

        postIdIndex.computeIfPresent(
                target.get().getPostId(),
                (k, idx) -> {
                    idx.remove(target.get().getCreatedAt());
                    return idx;
                });
    }
}
