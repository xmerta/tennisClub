package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.controller.dto.ReservationDto;
import cz.xmerta.tennisclub.controller.dto.mapper.ReservationDtoMapper;
import cz.xmerta.tennisclub.service.ReservationService;
import cz.xmerta.tennisclub.storage.model.Reservation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Controller class for Reservation, throws 400 if receives BAD REQUEST.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController{

    private final ReservationService reservationService;
    private final ReservationDtoMapper reservationDtoMapper;
    public ReservationController(ReservationService reservationService, ReservationDtoMapper reservationDtoMapper) {
        this.reservationService = reservationService;
        this.reservationDtoMapper = reservationDtoMapper;
    }

    /**
     * Fetch all reservations.
     *
     * @return collection of all reservations as DTOs
     */
    @GetMapping
    public ResponseEntity<Collection<ReservationDto>> getAllReservations() {
        Collection<ReservationDto> reservations = reservationService.findAll()
                .stream()
                .map(reservationDtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    /**
     * Fetch a reservation by its ID.
     *
     * @param id the ID of the reservation to fetch
     * @return ResponseEntity with the reservation DTO if found, otherwise 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable long id) {
        Optional<ReservationDto> reservation = reservationService.findById(id)
                .map(reservationDtoMapper::toDTO);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Delete a reservation by its ID.
     *
     * @param id the ID of the reservation to delete
     * @return ResponseEntity with the 204 if successfully deleted, 404 if not found
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
     * @return ResponseEntity with the 204 when all reservations are successfully deleted
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllReservations() {
        reservationService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * Create a new reservation.
     *
     * @param reservationDto the reservation DTO to create
     * @return ResponseEntity with the price of the created reservation, 400 if invalid
     */
    @PostMapping
    public ResponseEntity<Double> createReservation(
            @Valid @RequestBody ReservationDto reservationDto) {
        reservationDto.setId(null);
        var reservation = reservationDtoMapper.toEntity(reservationDto, reservationService.getCourtService());
        double price = reservationService.save(reservation).getPrice();
        return ResponseEntity.status(HttpStatus.CREATED).body(price);
    }
    /**
     * Update an existing reservation.
     *
     * @param id the ID of the reservation to update, must not be null
     * @param reservationDto the updated reservation details
     * @return ResponseEntity with the price of the updated reservation, or 400/404 if invalid
     */
    @PutMapping("/{id}")
    public ResponseEntity<Double> updateReservation(
            @PathVariable long id,
            @Valid @RequestBody ReservationDto reservationDto) {
        Optional<Reservation> existingReservation = reservationService.findById(id);

        if (existingReservation.isPresent()) {
            reservationDto.setId(existingReservation.get().getId());
            Reservation reservation = reservationDtoMapper.toEntity(reservationDto, reservationService.getCourtService());
            double price = reservationService.save(reservation).getPrice();
            return ResponseEntity.ok(price);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    /**
     * Fetch all reservations for a specific court.
     *
     * @param courtId the ID of the court
     * @return ResponseEntity with the collection of reservations for the specified court as DTOs
     */
    @GetMapping("/court/{courtId}")
    public ResponseEntity<Collection<ReservationDto>> getReservationsByCourt(@PathVariable long courtId) {
        Collection<ReservationDto> reservations = reservationService.getReservationsByCourtID(courtId)
                .stream()
                .map(reservationDtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }
    /**
     * Fetch reservations for a specific user by their phone number.
     *
     * @param phoneNumber the phone number of the user
     * @param pastReservations flag to include past reservations
     * @return ResponseEntity with collection of reservation for the user as DTOs
     */
    @GetMapping("/user/{phoneNumber}")
    public ResponseEntity<Collection<ReservationDto>> getReservationsByUser(
            @PathVariable String phoneNumber,
            @RequestParam(required = false, defaultValue = "false") boolean pastReservations) {

        Collection<ReservationDto> reservations;

        if (pastReservations) {
            reservations = reservationService.getReservationsByUserPhoneNumber(phoneNumber)
                    .stream()
                    .map(reservationDtoMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            reservations = reservationService.getUpcomingReservationsByUserPhoneNumber(phoneNumber)
                    .stream()
                    .map(reservationDtoMapper::toDTO)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(reservations);
    }
}

