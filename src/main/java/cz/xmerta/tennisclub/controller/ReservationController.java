package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.service.ReservationService;
import cz.xmerta.tennisclub.storage.model.Reservation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        if (reservationService.findById(id).isPresent()) {
            reservationService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Double> createReservation(
            @Valid @RequestBody Reservation reservation) {
        try {
            double price = reservationService.save(reservation).getPrice();
            return ResponseEntity.status(HttpStatus.CREATED).body(price);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/court/{courtId}")
    public ResponseEntity<Collection<Reservation>> getReservationsByCourt(@PathVariable long courtId) {
        return ResponseEntity.ok(reservationService.getReservationsByCourt(courtId));
    }

    @GetMapping("/user/{phoneNumber}")
    public ResponseEntity<Collection<Reservation>> getReservationsByUser(
            @PathVariable String phoneNumber,
            @RequestParam(required = false, defaultValue = "false") boolean includePast) {
        try {
            Collection<Reservation> reservations = !includePast
                    ? reservationService.getUpcomingReservationsByUser(phoneNumber)
                    : reservationService.getReservationsByUser(phoneNumber);
            return ResponseEntity.ok(reservations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

