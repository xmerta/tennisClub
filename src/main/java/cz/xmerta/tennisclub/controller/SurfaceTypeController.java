package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.controller.dto.SurfaceTypeDto;
import cz.xmerta.tennisclub.controller.dto.mapper.SurfaceTypeDtoMapper;
import cz.xmerta.tennisclub.service.SurfaceTypeService;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Controller class for SurfaceType, throws 400 if receives BAD REQUEST.
 */
@RestController
@RequestMapping("/api/surfacetypes")
public class SurfaceTypeController implements CrudController<SurfaceTypeDto> {

    private final SurfaceTypeService surfaceTypeService;
    private final SurfaceTypeDtoMapper surfaceTypeDtoMapper;

    public SurfaceTypeController(SurfaceTypeService surfaceTypeService, SurfaceTypeDtoMapper surfaceTypeDtoMapper) {
        this.surfaceTypeService = surfaceTypeService;
        this.surfaceTypeDtoMapper = surfaceTypeDtoMapper;
    }

    /**
     * Retrieves all surface types.
     *
     * @return ResponseEntity with   a collection of all surface type DTOs in the system.
     */
    @GetMapping
    public ResponseEntity<Collection<SurfaceTypeDto>> getAll() {
        Collection<SurfaceTypeDto> surfaceTypes = surfaceTypeService.findAll()
                .stream()
                .map(surfaceTypeDtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(surfaceTypes);
    }

    /**
     * Retrieves a surface type by its ID.
     *
     * @param id the ID of the surface type.
     * @return ResponseEntity with  the surface type DTO if found, or a 404 NOT FOUND status.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SurfaceTypeDto> getById(@PathVariable long id) {
        Optional<SurfaceTypeDto> surfaceTypeDTO = surfaceTypeService.findById(id).map(surfaceTypeDtoMapper::toDTO);
        ;
        return surfaceTypeDTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Creates a new surface type.
     *
     * @param surfaceTypeDto the surface type to create.
     * @return ResponseEntity with  the created surface type as DTO or a 400 BAD REQUEST status if invalid
     */
    @Override
    @PostMapping
    public ResponseEntity<SurfaceTypeDto> create(@RequestBody @Valid SurfaceTypeDto surfaceTypeDto) {
        surfaceTypeDto.setId(null);
        SurfaceType createdSurfaceType = surfaceTypeService.save(surfaceTypeDtoMapper.toEntity(surfaceTypeDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(surfaceTypeDtoMapper.toDTO(createdSurfaceType));
    }

    /**
     * Updates an existing surface type.
     *
     * @param id                 the ID of the surface type to update.
     * @param updatedSurfaceTypeDto the updated surface type data.
     * @return ResponseEntity with  the updated surface type DTO
     * 404 NOT FOUND status if the surface type does not exist
     * 400 if invalid data
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SurfaceTypeDto> update(@PathVariable long id, @RequestBody @Valid SurfaceTypeDto updatedSurfaceTypeDto) {

        Optional<SurfaceType> existingSurfaceType = surfaceTypeService.findById(id);
        if (existingSurfaceType.isPresent()) {
            updatedSurfaceTypeDto.setId(id);
            var savedSurfaceType = surfaceTypeService.save(surfaceTypeDtoMapper.toEntity(updatedSurfaceTypeDto));
            return ResponseEntity.ok(surfaceTypeDtoMapper.toDTO(savedSurfaceType));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Deletes a surface type by its ID.
     *
     * @param id the ID of the surface type to delete.
     * @return ResponseEntity with a 204 NO CONTENT status if the surface type was deleted, or a 404 NOT FOUND status if it does not exist.
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        Optional<SurfaceType> surfaceType = surfaceTypeService.findById(id);
        if (surfaceType.isPresent()) {
            surfaceTypeService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Deletes all surface types.
     *
     * @return ResponseEntity with a 204 NO CONTENT status after deletion.
     */
    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        surfaceTypeService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
