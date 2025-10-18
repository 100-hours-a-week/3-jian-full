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

@Repository
@Primary
public class CommentInMemoryRepository implements CommentRepository {

    private final InMemoryRepository<Comment> delegate;
    private final Map<Long, List<Comment>> postIdIndex =  new ConcurrentHashMap<>();

    public CommentInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public Comment save(Comment comment) {
        Comment saved = delegate.save(comment);
        postIdIndex.compute(saved.getPostId(), (k, list) ->
                Optional.ofNullable(list)
                        .map(l -> { l.add(saved); return l; })
                        .orElseGet(() -> new ArrayList<>(List.of(saved)))
        );
        return saved;
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return delegate.findById(commentId);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        List<Comment> comments = postIdIndex.get(postId);
        if (comments == null) return Collections.emptyList();
        return List.copyOf(comments); // 원본 배열 보호
    }

    @Override
    public CursorPage<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId, LocalDateTime cursor, int pageSize) {
        List<Comment> content = postIdIndex.get(postId).stream()
                .filter(comment -> cursor == null || comment.getCreatedAt().isBefore(cursor))
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .limit(pageSize + 1)
                .toList();
        boolean hasNext = content.size() > pageSize;

        return new CursorPage<>(content.subList(0, Math.min(content.size(), pageSize)), hasNext);
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
                (key, list) -> {
                    list.remove(target.get());
                    return list;
                }
        );
    }
}
