package com.strategy.test;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class OrphanRemovalTest {

    @Autowired
    EntityManager em;

    @Test
    void orphanRemoval_works_only_when_fk_is_null() {
        // given
        User user = new User();
        Post post = new Post();
        user.addPost(post);

        em.persist(user);
        em.flush();
        em.clear();

        // when
        User foundUser = em.find(User.class, user.id);
        Post foundPost = foundUser.posts.getFirst();

        foundUser.removePost(foundPost); // post.setUser(null) 주석 상태로 테스트

        em.flush(); // ❗ flush 시점에서 DELETE 나오는지 확인
    }

    @Entity
    public class User {
        @Id
        @GeneratedValue
        public Long id;

        @OneToMany(mappedBy = "user", orphanRemoval = true)
        public List<Post> posts = new ArrayList<>();

        public void addPost(Post post) {
            posts.add(post);
            post.setUser(this);
        }

        public void removePost(Post post) {
            posts.remove(post);
            // post.setUser(null); // ❗ 실험1: 주석 처리 / 실험2: 주석 해제
        }
    }

    @Entity
    public class Post {
        @Id
        @GeneratedValue
        public Long id;

        @ManyToOne
        @JoinColumn(name = "user_id")
        public User user;

        public void setUser(User user) {
            this.user = user;
        }
    }
}

