package com.jian.community.infrastructure.memory;

import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.Post;
import com.jian.community.domain.repository.PostRepository;
import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class PostInMemoryRepository implements PostRepository {

    private final InMemoryRepository<Post> delegate;

    PostInMemoryRepository(AtomicLongIdGenerator idGenerator) {
        this.delegate = new InMemoryRepository<>(idGenerator);
    }

    @Override
    public Post save(Post post) {
        return delegate.save(post);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return delegate.findById(postId);
    }

    @Override
    public void deleteById(Long postId) {
        delegate.deleteById(postId);
    }

    @Override
    public boolean existsById(Long postId) {
        return delegate.existsById(postId);
    }

    @Override
    public CursorPage<Post> findAllOrderByCreatedAtDesc(LocalDateTime cursor, int pageSize) {
        List<Post> content = delegate.findAll().stream()
                .filter(post -> cursor == null || post.getCreateAt().isBefore(cursor))
                .sorted(Comparator.comparing(Post::getCreateAt).reversed())
                .limit(pageSize + 1)
                .toList();
        boolean hasNext = content.size() > pageSize;

        return new CursorPage<>(content.subList(0, Math.min(content.size(), pageSize)), hasNext);
    }
}
