package cz.xmerta.tennisclub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CourtDto {

    private Long id;

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters.")
    private String name;

    @NotNull(message = "SurfaceType ID cannot be null.")
    private Long surfaceTypeId;

    public CourtDto(Long id, String name, Long surfaceTypeId) {
        this.id = id;
        this.name = name;
        this.surfaceTypeId = surfaceTypeId;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSurfaceTypeId() {
        return surfaceTypeId;
    }

    public void setSurfaceTypeId(Long surfaceTypeId) {
        this.surfaceTypeId = surfaceTypeId;
    }
}
