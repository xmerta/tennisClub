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
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<Collection<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable long id) {
        return reservationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        if (reservationService.findById(id).isPresent()) {
            reservationService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllReservations() {
        reservationService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<Double> createReservation(
            @Valid @RequestBody Reservation reservation) {
        if (reservation.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        double price = reservationService.save(reservation).getPrice();
        return ResponseEntity.status(HttpStatus.CREATED).body(price);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Double> updateReservation(
            @PathVariable long id,
            @Valid @RequestBody Reservation updatedReservation) {
        if (updatedReservation.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Reservation> existingReservation = reservationService.findById(id);

        if (existingReservation.isPresent()) {
            updatedReservation.setId(existingReservation.get().getId());
            double price = reservationService.save(updatedReservation).getPrice();
            return ResponseEntity.ok(price);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/court/{courtId}")
    public ResponseEntity<Collection<Reservation>> getReservationsByCourt(@PathVariable long courtId) {
        return ResponseEntity.ok(reservationService.getReservationsByCourt(courtId));
    }

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

