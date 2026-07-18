package com.qataruniversity.hms.repository;

import com.qataruniversity.hms.domain.Identifiable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryRepository<T extends Identifiable> implements CrudRepository<T> {
    private final Map<String, T> storage = new LinkedHashMap<>();

    @Override
    public synchronized T save(T entity) {
        Objects.requireNonNull(entity, "entity is required");
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public synchronized Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public synchronized List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public synchronized boolean existsById(String id) {
        return storage.containsKey(id);
    }

    @Override
    public synchronized boolean deleteById(String id) {
        return storage.remove(id) != null;
    }

    @Override
    public synchronized long count() {
        return storage.size();
    }
}
