package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.CourtDao;
import cz.xmerta.tennisclub.storage.model.Court;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
/**
 * Service class for managing {@link Court} entities. Provides CRUD operations and additional logic such as validation.
 */
@Service
@Transactional
public class CourtService implements CrudService<Court> {

    private final CourtDao courtDao;
    private final SurfaceTypeService  surfaceTypeService;
    /**
     * Constructor
     *
     * @param courtDao the DAO for managing {@link Court} entities
     * @param surfaceTypeService the service for surfaceType validation
     */
    public CourtService(CourtDao courtDao, SurfaceTypeService surfaceTypeService) {
        this.courtDao = courtDao;
        this.surfaceTypeService = surfaceTypeService;
    }

    /**
     * Saves a {@link Court} entity, ensuring its SurfaceType ID is valid and its name is unique.
     *
     * @param court the Court entity to save
     * @return the saved Court entity
     * @throws IllegalArgumentException if the SurfaceType ID is invalid or the Court name is not unique
     */
    @Override
    public Court save(Court court) {
        if (surfaceTypeService.findById(court.getSurfaceType().getId()).isEmpty()) {
            throw new IllegalArgumentException("Invalid SurfaceType ID: " + court.getSurfaceType().getId());
        }
        validateUniqueName(court);

        return courtDao.save(court);
    }

    /**
     * Validates that the name of the given Court is unique.
     *
     * @param court the Court entity to validate
     * @throws IllegalArgumentException if the Court name is not unique,
     * but does not throw if the ID is same as it means update.
     */
    private void validateUniqueName(Court court) {
        Optional<Court> existingCourt = courtDao.findByName(court.getName());
        if (existingCourt.isPresent() && !existingCourt.get().getId().equals(court.getId())) {
            throw new IllegalArgumentException("Court name must be unique: " + court.getName());
        }
    }
    /**
     * Finds a {@link Court} entity by its ID.
     *
     * @param id the ID of the Court to find
     * @return an {@link Optional} containing the found Court, or empty if not found
     */
    @Override
    public Optional<Court> findById(long id) {
        return courtDao.findById(id);
    }

    /**
     * Retrieves all {@link Court} entities.
     *
     * @return a collection of all Court entities
     */
    @Override
    public Collection<Court> findAll() {
        return courtDao.findAll();
    }

    /**
     * Deletes a {@link Court} entity by its ID.
     *
     * @param id the ID of the Court to delete
     */
    @Override
    public void deleteById(long id) {
        courtDao.deleteById(id);
    }

    /**
     * Deletes all {@link Court} entities.
     */
    @Override
    public void deleteAll() {
        courtDao.deleteAll();
    }

    /**
     * Retrieves the {@link SurfaceTypeService} used by this service.
     *
     * @return the SurfaceTypeService instance
     */
    public SurfaceTypeService getSurfaceTypeService() {
        return surfaceTypeService;
    }
}
