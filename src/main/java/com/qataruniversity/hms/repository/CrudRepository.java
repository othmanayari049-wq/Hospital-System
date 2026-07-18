package com.qataruniversity.hms.repository;

import com.qataruniversity.hms.domain.Identifiable;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends Identifiable> {
    T save(T entity);
    Optional<T> findById(String id);
    List<T> findAll();
    boolean existsById(String id);
    boolean deleteById(String id);
    long count();
}
