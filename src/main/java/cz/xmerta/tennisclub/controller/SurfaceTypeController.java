package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.service.SurfaceTypeService;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/surfacetypes")
public class SurfaceTypeController implements CrudController<SurfaceType> {

    private final SurfaceTypeService surfaceTypeService;

    public SurfaceTypeController(SurfaceTypeService surfaceTypeService) {
        this.surfaceTypeService = surfaceTypeService;
    }
    /**
     * Retrieves all surface types.
     *
     * @return a collection of all surface types in the system.
     */
    @Override
    @GetMapping
    public ResponseEntity<Collection<SurfaceType>> getAll() {
        Collection<SurfaceType> surfaceTypes = surfaceTypeService.findAll();
        return ResponseEntity.ok(surfaceTypes);
    }
    /**
     * Retrieves a surface type by its ID.
     *
     * @param id the ID of the surface type.
     * @return the surface type if found, or a 404 NOT FOUND status.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SurfaceType> getById(@PathVariable long id) {
        Optional<SurfaceType> surfaceType = surfaceTypeService.findById(id);
        return surfaceType
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    /**
     * Creates a new surface type.
     *
     * @param surfaceType the surface type to create.
     * @return the created surface type or a 400 BAD REQUEST status if invalid
     */
    @Override
    @PostMapping
    public ResponseEntity<SurfaceType> create(@RequestBody @Valid SurfaceType surfaceType) {
        surfaceType.setId(null);

        SurfaceType createdSurfaceType = surfaceTypeService.save(surfaceType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSurfaceType);
    }
    /**
     * Updates an existing surface type.
     *
     * @param id the ID of the surface type to update.
     * @param updatedSurfaceType the updated surface type data.
     * @return the updated surface type
     * 404 NOT FOUND status if the surface type does not exist
     * 400 if invalid data
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SurfaceType> update(@PathVariable long id, @RequestBody @Valid SurfaceType updatedSurfaceType) {

        Optional<SurfaceType> existingSurfaceType = surfaceTypeService.findById(id);
        if (existingSurfaceType.isPresent()) {
            updatedSurfaceType.setId(id);
            SurfaceType savedSurfaceType = surfaceTypeService.save(updatedSurfaceType);
            return ResponseEntity.ok(savedSurfaceType);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    /**
     * Deletes a surface type by its ID.
     *
     * @param id the ID of the surface type to delete.
     * @return a 204 NO CONTENT status if the surface type was deleted, or a 404 NOT FOUND status if it does not exist.
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
     * @return a 204 NO CONTENT status after deletion.
     */
    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        surfaceTypeService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
