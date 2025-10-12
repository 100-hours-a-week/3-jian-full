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
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Primary
public class CommentInMemoryRepository implements CommentRepository {

    private final InMemoryRepository<Comment> delegate;
    private final Map<Long, List<Long>> postIdIndex =  new ConcurrentHashMap<>();

    public CommentInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public Comment save(Comment comment) {
        Comment saved = delegate.save(comment);
        postIdIndex
                .computeIfAbsent(saved.getPostId(), key -> new CopyOnWriteArrayList<>())
                .add(saved.getId());
        return saved;
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return delegate.findById(commentId);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        List<Long> commentIds = postIdIndex.get(postId);
        if (commentIds == null || commentIds.isEmpty()) return Collections.emptyList();

        return commentIds.stream()
                .map(this::findById)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public CursorPage<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId, LocalDateTime cursor, int pageSize) {
        List<Comment> content = delegate.findAll().stream()
                .filter(comment -> cursor == null || comment.getCreatedAt().isBefore(cursor))
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .limit(pageSize + 1)
                .toList();
        boolean hasNext = content.size() > pageSize;

        return new CursorPage<>(content.subList(0, Math.min(content.size(), pageSize)), hasNext);
    }

    @Override
    public void deleteById(Long commentId) {
        delegate.deleteById(commentId);
    }
}
