package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.controller.dto.CourtDto;
import cz.xmerta.tennisclub.controller.dto.mapper.CourtDtoMapper;
import cz.xmerta.tennisclub.service.CourtService;
import cz.xmerta.tennisclub.storage.model.Court;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class for Court, throws 400 if receives BAD REQUEST.
 */
@RestController
@RequestMapping("/api/courts")
public class CourtController implements CrudController<CourtDto> {

    private final CourtService courtService;
    private final CourtDtoMapper courtDtoMapper;
    public CourtController(CourtService courtService, CourtDtoMapper courtDtoMapper) {
        this.courtService = courtService;
        this.courtDtoMapper = courtDtoMapper;
    }
    /**
     * Fetch all courts.
     *
     * @return ResponseEntity with collection of all courts
     */
    @Override
    @GetMapping
    public ResponseEntity<Collection<CourtDto>> getAll() {
        Collection<CourtDto> courts = courtService.findAll()
                .stream()
                .map(courtDtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courts);
    }

    /**
     * Fetch a court by its ID.
     *
     * @param id the ID of the court to fetch
     * @return ResponseEntity with the courtDTO if found, otherwise 404
     */
     @Override
     @GetMapping("/{id}")
     public ResponseEntity<CourtDto> getById(@PathVariable long id) {
         Optional<CourtDto> courtDto = courtService.findById(id)
                 .map(courtDtoMapper::toDTO);
         return courtDto.map(ResponseEntity::ok)
                 .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
     }
    /**
     * Create a new court.
     *
     * @param courtDto the court to create
     * @return ResponseEntity with the created courtDTO, or 400 if invalid
     */
    @Override
    @PostMapping
    public ResponseEntity<CourtDto> create(@RequestBody @Valid CourtDto courtDto) {
        courtDto.setId(null);
        Court createdCourt = courtService.save(courtDtoMapper.toEntity(courtDto, courtService.getSurfaceTypeService()));
        return ResponseEntity.status(HttpStatus.CREATED).body(courtDtoMapper.toDTO(createdCourt));
    }
    /**
     * Update an existing court.
     *
     * @param id the ID of the court to update, must not be null
     * @param updatedCourt the updated court details
     * @return ResponseEntity with the updated court, or 400/404 if invalid
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CourtDto> update(
            @PathVariable long id,
            @Valid @RequestBody CourtDto updatedCourt) {
        Optional<Court> existingCourt = courtService.findById(id);

        if (existingCourt.isPresent()) {
            updatedCourt.setId(existingCourt.get().getId());
            var savedCourt = courtService.save(courtDtoMapper.toEntity(updatedCourt, courtService.getSurfaceTypeService()));
            return ResponseEntity.ok(courtDtoMapper.toDTO(savedCourt));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    /**
     * Delete a court by its ID.
     *
     * @param id the ID of the court to delete
     * @return ResponseEntity with the 204 if successfully deleted, 404 if not found
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
     * @return ResponseEntity with the 204 when all courts are successfully deleted
     */
    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        courtService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
