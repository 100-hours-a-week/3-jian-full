package com.jian.community.infrastructure.memory;

import com.jian.community.infrastructure.util.AtomicLongIdGenerator;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.jian.community.infrastructure.util.InMemoryReflectionHelper.*;

@AllArgsConstructor
public class InMemoryRepository<T> {

    private final AtomicLongIdGenerator idGenerator;

    private final Map<Long, T> store = new ConcurrentHashMap<>();

    public T save(T entity) {
        LocalDateTime now = LocalDateTime.now();
        Long id = getId(entity);

        if (id == null) {
            id = idGenerator.nextId();
            setId(entity, id);
            setCreatedAt(entity, now);
            setUpdatedAt(entity, now);

        } else {
            setUpdatedAt(entity, now);
        }

        store.put(id, entity);
        return store.get(id);
    }

    public List<T> findAll() {
        return store.values().stream().toList();
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }
}
