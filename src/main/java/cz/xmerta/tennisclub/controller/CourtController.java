package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.service.CourtService;
import cz.xmerta.tennisclub.storage.model.Court;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/courts")
public class CourtController implements CrudController<Court> {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }
    /**
     * Fetch all courts.
     *
     * @return collection of all courts
     */
    @Override
    @GetMapping
    public ResponseEntity<Collection<Court>> getAll() {
        Collection<Court> courts = courtService.findAll();
        return ResponseEntity.ok(courts);
    }
    /**
     * Fetch a court by its ID.
     *
     * @param id the ID of the court to fetch
     * @return the court if found, otherwise 404
     */
     @Override
    @GetMapping("/{id}")
    public ResponseEntity<Court> getById(@PathVariable long id) {
        Optional<Court> court = courtService.findById(id);
        return court
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    /**
     * Create a new court.
     *
     * @param court the court to create
     * @return the created court, or 400 if invalid
     */
    @Override
    @PostMapping
    public ResponseEntity<Court> create(@Valid @RequestBody Court court) {
        court.setId(null);
        Court createdCourt = courtService.save(court);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourt);
    }
    /**
     * Update an existing court.
     *
     * @param id the ID of the court to update, must not be null
     * @param updatedCourt the updated court details
     * @return the updated court, or 400/404 if invalid
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Court> update(
            @PathVariable long id,
            @Valid @RequestBody Court updatedCourt) {
        Optional<Court> existingCourt = courtService.findById(id);

        if (existingCourt.isPresent()) {
            updatedCourt.setId(existingCourt.get().getId());
            Court savedCourt = courtService.save(updatedCourt);
            return ResponseEntity.ok(savedCourt);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    /**
     * Delete a court by its ID.
     *
     * @param id the ID of the court to delete
     * @return 204 if successfully deleted, 404 if not found
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        Optional<Court> court = courtService.findById(id);
        if (court.isPresent()) {
            courtService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    /**
     * Delete all courts.
     *
     * @return 204 when all courts are successfully deleted
     */
    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        courtService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
