package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.CourtDao;
import cz.xmerta.tennisclub.storage.model.Court;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class CourtService implements CrudService<Court> {

    private final CourtDao courtDao;
    private final SurfaceTypeService  surfaceTypeService;

    public CourtService(CourtDao courtDao, SurfaceTypeService surfaceTypeService) {
        this.courtDao = courtDao;
        this.surfaceTypeService = surfaceTypeService;
    }

    @Override
    public Court save(Court court) {
        if (surfaceTypeService.findById(court.getSurfaceType().getId()).isEmpty()) {
            throw new IllegalArgumentException("Invalid SurfaceType ID: " + court.getSurfaceType().getId());
        }
        validateUniqueName(court);

        return courtDao.save(court);
    }

    private void validateUniqueName(Court court) {
        Optional<Court> existingCourt = courtDao.findByName(court.getName());
        if (existingCourt.isPresent() && !existingCourt.get().getId().equals(court.getId())) {
            throw new IllegalArgumentException("Court name must be unique: " + court.getName());
        }
    }

    @Override
    public Optional<Court> findById(long id) {
        return courtDao.findById(id);
    }

    @Override
    public Collection<Court> findAll() {
        return courtDao.findAll();
    }

    @Override
    public void deleteById(long id) {
        courtDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        courtDao.deleteAll();
    }
}
