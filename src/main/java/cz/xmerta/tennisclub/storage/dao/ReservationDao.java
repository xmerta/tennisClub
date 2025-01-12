package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.model.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao implements DataAccessObject<Reservation> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            entityManager.persist(reservation);
        } else {
            reservation = entityManager.merge(reservation);
        }
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        return Optional.ofNullable(reservation);
    }

    @Override
    public List<Reservation> findAll() {
        return entityManager.createQuery("SELECT r FROM Reservation r", Reservation.class).getResultList();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Reservation").executeUpdate();
    }

    public List<Reservation> findByCourtId(Long courtId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r WHERE r.court.id = :courtId", Reservation.class)
                .setParameter("courtId", courtId)
                .getResultList();
    }

    public List<Reservation> findByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r WHERE r.user.id = :userId", Reservation.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
