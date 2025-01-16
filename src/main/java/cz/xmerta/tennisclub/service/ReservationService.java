package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.ReservationDao;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.GameType;
import cz.xmerta.tennisclub.storage.model.Reservation;
import cz.xmerta.tennisclub.storage.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService implements CrudService<Reservation> {

    private final ReservationDao reservationDao;
    private final CourtService courtService;
    private final UserService userService;

    public ReservationService(ReservationDao reservationDao, CourtService courtService, UserService userService) {
        this.reservationDao = reservationDao;
        this.courtService = courtService;
        this.userService = userService;
    }

    @Override
    public Reservation save(Reservation reservation) {
        validateCourtExists(reservation);
        ensureUserExists(reservation);
        validateReservation(reservation);
        reservation.setPrice(calculatePrice(reservation));
        return reservationDao.save(reservation);
    }


    private double calculatePrice(Reservation reservation) {
        double basePrice = reservation.getCourt().getSurfaceType().getPricePerMinute();
        double multiplier = reservation.getGameType() == GameType.DOUBLE ? 1.5 : 1.0;
        return basePrice * multiplier * Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes();
    }

    /**
     *
     * @param reservation
     * Might require specialized Database query for optimization in the future.
     */
    private void validateReservation(Reservation reservation) {
        Collection<Reservation> allReservations = reservationDao.findAll();

        boolean overlaps = allReservations.stream()
                .filter(existingReservation -> Objects.equals(existingReservation.getCourt().getId(), reservation.getCourt().getId()))
                .anyMatch(existingReservation ->
                        reservation.getStartTime().isBefore(existingReservation.getEndTime()) &&
                                reservation.getEndTime().isAfter(existingReservation.getStartTime())
                );

        if (overlaps) {
            throw new IllegalArgumentException("The reservation time overlaps with an existing reservation.");
        }
    }

    private void validateCourtExists(Reservation reservation) {
        Optional<Court> court = courtService.findById(reservation.getCourt().getId());
        if (court.isEmpty()) {
            throw new IllegalArgumentException("Court with ID " + reservation.getCourt().getId() + " does not exist.");
        }
    }

    private void ensureUserExists(Reservation reservation) {
        Optional<User> user = userService.findByPhoneNumber(reservation.getUser().getPhoneNumber());
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setName(reservation.getUser().getName());
            newUser.setPhoneNumber(reservation.getUser().getPhoneNumber());
            User createdUser = userService.save(newUser);
            reservation.setUser(createdUser);
        } else {
            reservation.setUser(user.get());
        }
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

    public Collection<Reservation> getReservationsByUser(String phoneNumber) {
        long userId = userService.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User with phone number " + phoneNumber + " not found."))
                .getId();
        return reservationDao.findByUserId(userId);
    }

    public Collection<Reservation> getUpcomingReservationsByUser(String phoneNumber) {
        return getReservationsByUser(phoneNumber).stream()
                .filter(reservation -> reservation.getEndTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }
}
