package com.jian.community.infrastructure.memory;

import com.jian.community.domain.model.Post;
import com.jian.community.domain.repository.PostRepository;
import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

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
}
