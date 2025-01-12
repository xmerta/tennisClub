package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.model.SurfaceType;
import cz.xmerta.tennisclub.storage.dao.SurfaceTypeDao;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SurfaceTypeService implements CrudService<SurfaceType> {

    private final SurfaceTypeDao surfaceTypeDao;

    public SurfaceTypeService(SurfaceTypeDao surfaceTypeDao) {
        this.surfaceTypeDao = surfaceTypeDao;
    }

    @Override
    public SurfaceType save(SurfaceType surfaceType) {
        return surfaceTypeDao.save(surfaceType);
    }

    @Override
    public Optional<SurfaceType> findById(long id) {
        return surfaceTypeDao.findById(id);
    }

    @Override
    public Collection<SurfaceType> findAll() {
        return surfaceTypeDao.findAll();
    }

    @Override
    public void deleteById(long id) {
        surfaceTypeDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        surfaceTypeDao.deleteAll();
    }
}
