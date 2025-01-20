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

    public ReservationDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r WHERE r.id = :id AND r.isDeleted = false", Reservation.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r WHERE r.isDeleted = false", Reservation.class)
                .getResultList();
    }

    @Override
    public void deleteById(Long id) {
        entityManager.createQuery("UPDATE Reservation r SET r.isDeleted = true WHERE r.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("UPDATE Reservation r SET r.isDeleted = true")
                .executeUpdate();
    }

    public List<Reservation> findByCourtId(Long courtId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r WHERE r.court.id = :courtId AND r.isDeleted = false", Reservation.class)
                .setParameter("courtId", courtId)
                .getResultList();
    }

    public List<Reservation> findByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.isDeleted = false", Reservation.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
