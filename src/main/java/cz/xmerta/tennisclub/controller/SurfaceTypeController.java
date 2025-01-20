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

    @Override
    @GetMapping
    public ResponseEntity<Collection<SurfaceType>> getAll() {
        Collection<SurfaceType> surfaceTypes = surfaceTypeService.findAll();
        return ResponseEntity.ok(surfaceTypes);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SurfaceType> getById(@PathVariable long id) {
        Optional<SurfaceType> surfaceType = surfaceTypeService.findById(id);
        return surfaceType
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    @PostMapping
    public ResponseEntity<SurfaceType> create(@RequestBody @Valid SurfaceType surfaceType) {
        if (surfaceType.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        SurfaceType createdSurfaceType = surfaceTypeService.save(surfaceType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSurfaceType);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SurfaceType> update(@PathVariable long id, @RequestBody @Valid SurfaceType updatedSurfaceType) {
        if (updatedSurfaceType.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<SurfaceType> existingSurfaceType = surfaceTypeService.findById(id);
        if (existingSurfaceType.isPresent()) {
            updatedSurfaceType.setId(id);
            SurfaceType savedSurfaceType = surfaceTypeService.save(updatedSurfaceType);
            return ResponseEntity.ok(savedSurfaceType);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

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

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        surfaceTypeService.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
