package cz.xmerta.tennisclub.storage.dao;

import java.util.Collection;
import java.util.Optional;

public interface DataAccessObject<E> {
    E save(E entity);

    Collection<E> findAll();

    Optional<E> findById(Long id);

    void deleteById(Long id);

    void deleteAll();
}

