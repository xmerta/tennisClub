package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.model.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class SurfaceTypeDao implements DataAccessObject<SurfaceType> {

    @PersistenceContext
    private EntityManager entityManager;

    public SurfaceTypeDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public SurfaceType save(SurfaceType surfaceType) {
        if (surfaceType.getId() == null) {
            entityManager.persist(surfaceType);
        } else {
            surfaceType = entityManager.merge(surfaceType);
        }
        return surfaceType;
    }

    @Override
    public Collection<SurfaceType> findAll() {
        return entityManager.createQuery("SELECT s FROM SurfaceType s WHERE s.isDeleted = false", SurfaceType.class)
                .getResultList();
    }

    @Override
    public Optional<SurfaceType> findById(Long id) {
        return entityManager.createQuery("SELECT s FROM SurfaceType s WHERE s.id = :id AND s.isDeleted = false", SurfaceType.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        entityManager.createQuery("UPDATE SurfaceType s SET s.isDeleted = true WHERE s.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("UPDATE SurfaceType s SET s.isDeleted = true")
                .executeUpdate();
    }

    /**
     * Added for optimalization.
     * @param name
     * @return
     */
    public Optional<SurfaceType> findByName(String name) {
        return entityManager.createQuery(
                        "SELECT s FROM SurfaceType s WHERE s.name = :name AND s.isDeleted = false", SurfaceType.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }
}