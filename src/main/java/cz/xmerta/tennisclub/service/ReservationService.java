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
/**
 * Service class for managing {@link Reservation} entities. Provides CRUD operations and additional
 * business logic such as validation, pricing, and retrieval by user or court.
 */
@Service
@Transactional
public class ReservationService implements CrudService<Reservation> {

    private final ReservationDao reservationDao;
    private final CourtService courtService;
    private final UserService userService;
    /**
     * Constructor for {@link ReservationService}.
     *
     * @param reservationDao the DAO for managing {@link Reservation} entities
     * @param courtService the service for managing {@link Court} entities
     * @param userService the service for managing {@link User} entities
     */
    public ReservationService(ReservationDao reservationDao, CourtService courtService, UserService userService) {
        this.reservationDao = reservationDao;
        this.courtService = courtService;
        this.userService = userService;
    }

    /**
     * Saves a {@link Reservation}, performing validation, pricing calculation.
     * Checks whether user exists, if not saves his name and number via userService.
     *
     * @param reservation the Reservation entity to save
     * @return the saved Reservation entity
     * @throws IllegalArgumentException if the court does not exist, the reservation time overlaps,
     *                                  or any other validation fails
     */
    @Override
    public Reservation save(Reservation reservation) {
        validateCourtExists(reservation);
        ensureUserExists(reservation);
        validateReservationTime(reservation);
        reservation.setPrice(calculatePrice(reservation));
        return reservationDao.save(reservation);
    }

    /**
     * Calculates the price for a reservation based on court pricing and game type.
     * This is saved in case pricing of particular court changes after reservation was done.
     *
     * @param reservation the Reservation entity
     * @return the calculated price
     */
    private double calculatePrice(Reservation reservation) {
        double basePrice = reservation.getCourt().getSurfaceType().getPricePerMinute();
        double multiplier = reservation.getGameType() == GameType.DOUBLE ? 1.5 : 1.0;
        return basePrice * multiplier * Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes();
    }
    /**
     * Validates that the reservation time does not overlap with existing reservations for the same court.
     *
     * @param reservation the Reservation entity to validate
     * @throws IllegalArgumentException if the reservation time overlaps with an existing reservation
     */
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
    /**
     * Validates that the court associated with the reservation exists.
     *
     * @param reservation the Reservation entity to validate
     * @throws IllegalArgumentException if the court does not exist
     */
    private void validateCourtExists(Reservation reservation) {
        Optional<Court> court = courtService.findById(reservation.getCourt().getId());
        if (court.isEmpty()) {
            throw new IllegalArgumentException("Court with ID " + reservation.getCourt().getId() + " does not exist.");
        }
    }
    /**
     * Ensures the user associated with the reservation exists, creating a new user if necessary.
     *
     * @param reservation the Reservation entity to process
     */
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
    /**
     * Finds a {@link Reservation} by its ID.
     *
     * @param id the ID of the Reservation to find
     * @return an {@link Optional} containing the found Reservation, or empty if not found
     */
    @Override
    public Optional<Reservation> findById(long id) {
        return reservationDao.findById(id);
    }
    /**
     * Retrieves all {@link Reservation} entities.
     *
     * @return a collection of all Reservation entities
     */
    @Override
    public Collection<Reservation> findAll() {
        return reservationDao.findAll();
    }
    /**
     * Deletes a {@link Reservation} by its ID.
     *
     * @param id the ID of the Reservation to delete
     */
    @Override
    public void deleteById(long id) {
        reservationDao.deleteById(id);
    }
    /**
     * Deletes all {@link Reservation} entities.
     */
    @Override
    public void deleteAll() {
        reservationDao.deleteAll();
    }
    /**
     * Retrieves reservations for a specific court by its ID.
     *
     * @param courtId the ID of the court
     * @return a collection of reservations for the specified court
     */
    public Collection<Reservation> getReservationsByCourtID(long courtId) {
        return reservationDao.findByCourtId(courtId);
    }
    /**
     * Retrieves reservations for a user based on their phone number.
     *
     * @param phoneNumber the phone number of the user
     * @return a collection of reservations associated with the user
     * @throws IllegalArgumentException if no user is found with the given phone number
     */
    public Collection<Reservation> getReservationsByUserPhoneNumber(String phoneNumber) {
        long userId = userService.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("User with phone number " + phoneNumber + " not found."))
                .getId();
        return reservationDao.findByUserId(userId);
    }
    /**
     * Retrieves upcoming reservations for a user based on their phone number.
     *
     * @param phoneNumber the phone number of the user
     * @return a collection of upcoming reservations for the user
     */
    public Collection<Reservation> getUpcomingReservationsByUserPhoneNumber(String phoneNumber) {
        return getReservationsByUserPhoneNumber(phoneNumber).stream()
                .filter(reservation -> reservation.getEndTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }
    /**
     * Retrieves the {@link CourtService} used by this service.
     *
     * @return the CourtService instance
     */
    public CourtService getCourtService() {
        return courtService;
    }
}
