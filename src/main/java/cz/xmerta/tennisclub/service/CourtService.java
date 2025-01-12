package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.CourtDao;
import cz.xmerta.tennisclub.storage.entity.Court;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CourtService implements CrudService<Court> {

    private final CourtDao courtDao;

    public CourtService(CourtDao courtDao) {
        this.courtDao = courtDao;
    }

    @Override
    public Court save(Court court) {
        return courtDao.save(court);
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
