package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.service.ReservationService;
import cz.xmerta.tennisclub.storage.model.Reservation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController{

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Fetch all reservations.
     *
     * @return collection of all reservations
     */
    @GetMapping
    public ResponseEntity<Collection<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    /**
     * Fetch a reservation by its ID.
     *
     * @param id the ID of the reservation to fetch
     * @return the reservation if found, otherwise 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable long id) {
        return reservationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Delete a reservation by its ID.
     *
     * @param id the ID of the reservation to delete
     * @return 204 if successfully deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        if (reservationService.findById(id).isPresent()) {
            reservationService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    /**
     * Delete all reservations.
     *
     * @return 204 when all reservations are successfully deleted
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllReservations() {
        reservationService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * Create a new reservation.
     *
     * @param reservation the reservation to create
     * @return the price of the created reservation, 400 if invalid
     */
    @PostMapping
    public ResponseEntity<Double> createReservation(
            @Valid @RequestBody Reservation reservation) {
        reservation.setId(null);
        double price = reservationService.save(reservation).getPrice();
        return ResponseEntity.status(HttpStatus.CREATED).body(price);
    }
    /**
     * Update an existing reservation.
     *
     * @param id the ID of the reservation to update, must not be null
     * @param updatedReservation the updated reservation details
     * @return the price of the updated reservation, or 400/404 if invalid
     */
    @PutMapping("/{id}")
    public ResponseEntity<Double> updateReservation(
            @PathVariable long id,
            @Valid @RequestBody Reservation updatedReservation) {
        Optional<Reservation> existingReservation = reservationService.findById(id);

        if (existingReservation.isPresent()) {
            updatedReservation.setId(existingReservation.get().getId());
            double price = reservationService.save(updatedReservation).getPrice();
            return ResponseEntity.ok(price);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    /**
     * Fetch all reservations for a specific court.
     *
     * @param courtId the ID of the court
     * @return collection of reservations for the specified court
     */
    @GetMapping("/court/{courtId}")
    public ResponseEntity<Collection<Reservation>> getReservationsByCourt(@PathVariable long courtId) {
        return ResponseEntity.ok(reservationService.getReservationsByCourt(courtId));
    }
    /**
     * Fetch reservations for a specific user by their phone number.
     *
     * @param phoneNumber the phone number of the user
     * @param pastReservations flag to include past reservations
     * @return collection of reservations for the user
     */
    @GetMapping("/user/{phoneNumber}")
    public ResponseEntity<Collection<Reservation>> getReservationsByUser(
            @PathVariable String phoneNumber,
            @RequestParam(required = false, defaultValue = "false") boolean pastReservations) {

        Collection<Reservation> reservations = !pastReservations
                ? reservationService.getUpcomingReservationsByUser(phoneNumber)
                : reservationService.getReservationsByUser(phoneNumber);
        return ResponseEntity.ok(reservations);

    }
}

