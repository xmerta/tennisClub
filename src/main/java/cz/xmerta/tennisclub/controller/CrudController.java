package cz.xmerta.tennisclub.controller;

import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface CrudController<T> {

    ResponseEntity<Collection<T>> getAll();

    ResponseEntity<T> getById(long id);

    ResponseEntity<T> create(T entity);

    ResponseEntity<T> update(long id, T entity);

    ResponseEntity<Void> deleteById(long id);

    ResponseEntity<Void> deleteAll();
}
