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
        validateReservationTime(reservation);
        reservation.setPrice(calculatePrice(reservation));
        return reservationDao.save(reservation);
    }

    private double calculatePrice(Reservation reservation) {
        double basePrice = reservation.getCourt().getSurfaceType().getPricePerMinute();
        double multiplier = reservation.getGameType() == GameType.DOUBLE ? 1.5 : 1.0;
        return basePrice * multiplier * Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes();
    }

    private void validateReservationTime(Reservation reservation) {
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
            User newUser = new User(null,
                    reservation.getUser().getPhoneNumber(),
                    reservation.getUser().getName());
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

    public Collection<Reservation> getReservationsByCourtID(long courtId) {
        return reservationDao.findByCourtId(courtId);
    }

    public Collection<Reservation> getReservationsByUserPhoneNumber(String phoneNumber) {
        long userId = userService.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User with phone number " + phoneNumber + " not found."))
                .getId();
        return reservationDao.findByUserId(userId);
    }

    public Collection<Reservation> getUpcomingReservationsByUserPhoneNumber(String phoneNumber) {
        return getReservationsByUserPhoneNumber(phoneNumber).stream()
                .filter(reservation -> reservation.getEndTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public CourtService getCourtService() {
        return courtService;
    }
}
