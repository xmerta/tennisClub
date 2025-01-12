package cz.xmerta.tennisclub.service;

import java.util.Collection;
import java.util.Optional;

public interface CrudService<E> {

    E save(E entity);

    Optional<E> findById(long id);

    Collection<E> findAll();

    void deleteById(long id);

    void deleteAll();
}
