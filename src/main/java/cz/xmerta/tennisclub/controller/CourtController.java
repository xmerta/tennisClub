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

    @Override
    @GetMapping
    public ResponseEntity<Collection<Court>> getAll() {
        Collection<Court> courts = courtService.findAll();
        return ResponseEntity.ok(courts);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Court> getById(@PathVariable long id) {
        Optional<Court> court = courtService.findById(id);
        return court
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    @PostMapping
    public ResponseEntity<Court> create(@Valid @RequestBody Court court) {
        if (court.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Court createdCourt = courtService.save(court);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourt);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Court> update(
            @PathVariable long id,
            @Valid @RequestBody Court updatedCourt) {
        if (updatedCourt.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Court> existingCourt = courtService.findById(id);
        if (existingCourt.isPresent()) {
            Court savedCourt = courtService.save(updatedCourt);
            return ResponseEntity.ok(savedCourt);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

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

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        courtService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
