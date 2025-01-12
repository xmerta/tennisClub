package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.ReservationDao;
import cz.xmerta.tennisclub.storage.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ReservationService implements CrudService<Reservation> {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationDao.save(reservation);
    }

    @Override
    public Optional<Reservation> findById(long id) {
        return reservationDao.findById(id);
    }

    @Override
    public Collection<Reservation> findAll() {
        return reservationDao.findAll();
    }

    @Override
    public void deleteById(long id) {
        reservationDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        reservationDao.deleteAll();
    }

    public Collection<Reservation> getReservationsByCourt(long courtId) {
        return reservationDao.findByCourtId(courtId);
    }
}
