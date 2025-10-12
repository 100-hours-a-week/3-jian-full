package com.jian.community.domain.repository;

import com.jian.community.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    public Comment save(Comment comment);

    public Optional<Comment> findById(Long commentId);

    public List<Comment> findByPostId(Long postId);

    public void deleteById(Long commentId);
}
