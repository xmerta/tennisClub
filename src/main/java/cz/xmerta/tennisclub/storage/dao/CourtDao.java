package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.entity.Court;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class CourtDao implements DataAccessObject<Court> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Court save(Court court) {
        if (court.getId() == null) {
            entityManager.persist(court);
        } else {
            court = entityManager.merge(court);
        }
        return court;
    }

    @Override
    public Collection<Court> findAll() {
        return entityManager.createQuery("SELECT c FROM Court c", Court.class).getResultList();
    }

    @Override
    public Optional<Court> findById(Long id) {
        Court court = entityManager.find(Court.class, id);
        return Optional.ofNullable(court);
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Court").executeUpdate();
    }
}
