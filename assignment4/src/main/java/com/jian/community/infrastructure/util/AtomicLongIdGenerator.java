package com.jian.community.infrastructure.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class AtomicLongIdGenerator {

    private final AtomicLong sequence = new AtomicLong(0);

    public long nextId() {
        return sequence.incrementAndGet();
    }
}
