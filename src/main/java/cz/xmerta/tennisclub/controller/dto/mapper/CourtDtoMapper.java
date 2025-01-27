package cz.xmerta.tennisclub.controller.dto.mapper;

import cz.xmerta.tennisclub.controller.dto.CourtDto;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import cz.xmerta.tennisclub.service.SurfaceTypeService;
import org.springframework.stereotype.Component;
/**
 * Mapper class for converting between {@link Court} entities and {@link CourtDto} DTOs.
 */
@Component
public class CourtDtoMapper {

    /**
     * Converts a {@link Court} entity to a {@link CourtDto}.
     *
     * @param entity the Court entity to convert
     * @return a DTO representation of the given Court entity
     */
    public CourtDto toDTO(Court entity) {
        return new CourtDto(entity.getId(), entity.getName(), entity.getSurfaceType().getId());
    }
    /**
     * Converts a {@link CourtDto} to a {@link Court} entity.
     *
     * @param dto the DTO to convert
     * @param surfaceTypeService the service used to retrieve the {@link SurfaceType} by ID
     * @return a Court entity based on the provided DTO
     * @throws IllegalArgumentException if the SurfaceType ID in the DTO does not exist
     */
    public Court toEntity(CourtDto dto, SurfaceTypeService surfaceTypeService) {
        SurfaceType surfaceType = surfaceTypeService.findById(dto.getSurfaceTypeId())
                .orElseThrow(() -> new IllegalArgumentException("SurfaceType ID " + dto.getSurfaceTypeId() + " does not exist."));
        return new Court(dto.getId(), dto.getName(), surfaceType);
    }
}
