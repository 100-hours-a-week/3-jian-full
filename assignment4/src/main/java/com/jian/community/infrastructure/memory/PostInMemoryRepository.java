package com.jian.community.infrastructure.memory;

import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.Post;
import com.jian.community.domain.repository.PostRepository;
import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

@Repository
@Primary
public class PostInMemoryRepository implements PostRepository {

    private final InMemoryRepository<Post> delegate;
    private final ConcurrentSkipListMap<LocalDateTime, Post> createdAtIndex = new ConcurrentSkipListMap<>();

    PostInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public Post save(Post post) {
        Post saved = delegate.save(post);
        createdAtIndex.put(saved.getCreatedAt(), saved);
        return saved;
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return delegate.findById(postId);
    }

    @Override
    public void deleteById(Long postId) {
        removeIndices(postId);
        delegate.deleteById(postId);
    }

    @Override
    public CursorPage<Post> findAllOrderByCreatedAtDesc(LocalDateTime cursor, int pageSize) {
        NavigableMap<LocalDateTime, Post> window = (cursor == null)
                ? createdAtIndex.descendingMap()
                : createdAtIndex.headMap(cursor, false).descendingMap();

        List<Post> paged = window.values().stream()
                .limit(pageSize + 1)
                .toList();

        boolean hasNext = paged.size() > pageSize;

        return new CursorPage<>(paged.subList(0, Math.min(paged.size(), pageSize)), hasNext);
    }

    private void removeIndices(Long postId) {
        Optional<Post> target = findById(postId);
        if (target.isEmpty()) return;
        createdAtIndex.remove(target.get().getCreatedAt());
    }
}
