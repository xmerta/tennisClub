package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.model.Court;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class CourtDao implements DataAccessObject<Court> {
    @PersistenceContext
    private EntityManager entityManager;

    public CourtDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
        return entityManager.createQuery("SELECT c FROM Court c WHERE c.isDeleted = false", Court.class)
                .getResultList();
    }

    @Override
    public Optional<Court> findById(Long id) {
        return entityManager.createQuery("SELECT c FROM Court c WHERE c.id = :id AND c.isDeleted = false", Court.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    public Optional<Court> findByName(String name) {
        return entityManager.createQuery(
                        "SELECT c FROM Court c WHERE c.name = :name AND c.isDeleted = false", Court.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        entityManager.createQuery("UPDATE Court c SET c.isDeleted = true WHERE c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("UPDATE Court c SET c.isDeleted = true")
                .executeUpdate();
    }
}
