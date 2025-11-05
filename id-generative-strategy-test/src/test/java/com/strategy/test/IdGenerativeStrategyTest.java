package com.strategy.test;

import com.strategy.test.entity.Identity;
import com.strategy.test.entity.Sequence;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class IdGenerativeStrategyTest {

    @Autowired
    EntityManager em;

    @Test
    public void test_identity_strategy_1000() { // 770ms -> 992ms
        for (int i = 0; i < 1000; i++) {
            em.persist(new Identity());
        }
        em.flush();
        em.clear();
    }

    @Test
    public void test_sequence_strategy_1000() { // 604ms -> 495ms
        for (int i = 0; i < 1000; i++) {
            em.persist(new Sequence());
        }
        em.flush();
        em.clear();
    }

    @Test
    public void test_identity_strategy_10000() { // 2초 581ms -> 4초 678ms
        for (int i = 0; i < 10000; i++) {
            em.persist(new Identity());
        }
        em.flush();
        em.clear();
    }

    @Test
    public void test_sequence_strategy_10000() { // 2초 62ms -> 1초 493ms
        for (int i = 0; i < 10000; i++) {
            em.persist(new Sequence());
        }
        em.flush();
        em.clear();
    }

    @Test
    public void test_identity_strategy_100000() { // 35초 626ms ->
        for (int i = 0; i < 100000; i++) {
            em.persist(new Identity());
        }
        em.flush();
        em.clear();
    }

    @Test
    public void test_sequence_strategy_100000() { // 8초 623ms(50) -> 8초 154ms(100) -> 7초 162ms(1000)
        for (int i = 0; i < 100000; i++) {
            em.persist(new Sequence());
        }
        em.flush();
        em.clear();
    }
}
