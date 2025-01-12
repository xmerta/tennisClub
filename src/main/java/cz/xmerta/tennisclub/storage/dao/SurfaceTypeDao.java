package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.entity.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class SurfaceTypeDao implements DataAccessObject<SurfaceType> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SurfaceType save(SurfaceType surfaceType) {
        if (surfaceType.getId() == null) {
            entityManager.persist(surfaceType); // New entity
        } else {
            surfaceType = entityManager.merge(surfaceType); // Update existing
        }
        return surfaceType;
    }

    @Override
    public Collection<SurfaceType> findAll() {
        return entityManager.createQuery("SELECT s FROM SurfaceType s", SurfaceType.class).getResultList();
    }

    @Override
    public Optional<SurfaceType> findById(Long id) {
        SurfaceType surfaceType = entityManager.find(SurfaceType.class, id);
        return Optional.ofNullable(surfaceType);
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM SurfaceType").executeUpdate();
    }
}