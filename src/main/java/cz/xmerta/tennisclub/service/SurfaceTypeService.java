package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.model.SurfaceType;
import cz.xmerta.tennisclub.storage.dao.SurfaceTypeDao;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Service class for managing {@link SurfaceType} entities. Provides CRUD operations and ensures unique names for surface types.
 */
@Service
@Transactional
public class SurfaceTypeService implements CrudService<SurfaceType> {

    private final SurfaceTypeDao surfaceTypeDao;

    /**
     * Constructor for {@link SurfaceTypeService}.
     *
     * @param surfaceTypeDao the DAO for managing {@link SurfaceType} entities
     */
    public SurfaceTypeService(SurfaceTypeDao surfaceTypeDao) {
        this.surfaceTypeDao = surfaceTypeDao;
    }

    /**
     * Saves a {@link SurfaceType} entity after validating its name for uniqueness.
     *
     * @param surfaceType the SurfaceType entity to save
     * @return the saved SurfaceType entity
     * @throws IllegalArgumentException if the name is not unique
     */
    @Override
    public SurfaceType save(SurfaceType surfaceType) {
        validateUniqueName(surfaceType);
        return surfaceTypeDao.save(surfaceType);
    }

    /**
     * Validates that the name of the given {@link SurfaceType} is unique. If the ID matches an existing entry,
     * it is considered an update and no exception is thrown.
     *
     * @param surfaceType the SurfaceType entity to validate
     * @throws IllegalArgumentException if the name is not unique
     */
    private void validateUniqueName(SurfaceType surfaceType) {
        Optional<SurfaceType> existing = surfaceTypeDao.findByName(surfaceType.getName());
        if (existing.isPresent() && !existing.get().getId().equals(surfaceType.getId())) {
            throw new IllegalArgumentException("Surface type name must be unique: " + surfaceType.getName());
        }
    }

    /**
     * Finds a {@link SurfaceType} by its ID.
     *
     * @param id the ID of the SurfaceType to find
     * @return an {@link Optional} containing the found SurfaceType, or empty if not found
     */
    @Override
    public Optional<SurfaceType> findById(long id) {
        return surfaceTypeDao.findById(id);
    }

    /**
     * Retrieves all {@link SurfaceType} entities.
     *
     * @return a collection of all SurfaceType entities
     */
    @Override
    public Collection<SurfaceType> findAll() {
        return surfaceTypeDao.findAll();
    }

    /**
     * Deletes a {@link SurfaceType} by its ID.
     *
     * @param id the ID of the SurfaceType to delete
     */
    @Override
    public void deleteById(long id) {
        surfaceTypeDao.deleteById(id);
    }

    /**
     * Deletes all {@link SurfaceType} entities.
     */
    @Override
    public void deleteAll() {
        surfaceTypeDao.deleteAll();
    }
}
