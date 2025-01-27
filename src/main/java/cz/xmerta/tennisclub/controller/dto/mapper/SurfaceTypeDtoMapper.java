package cz.xmerta.tennisclub.controller.dto.mapper;

import cz.xmerta.tennisclub.controller.dto.SurfaceTypeDto;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link SurfaceType} entities and {@link SurfaceTypeDto} DTOs.
 */
@Component
public class SurfaceTypeDtoMapper {

    /**
     * Converts a {@link SurfaceType} entity to a {@link SurfaceTypeDto}.
     *
     * @param entity the SurfaceType entity to convert
     * @return a DTO representation of the given SurfaceType entity
     */
    public SurfaceTypeDto toDTO(SurfaceType entity) {
        return new SurfaceTypeDto(entity.getId(), entity.getName(), entity.getPricePerMinute());
    }

    /**
     * Converts a {@link SurfaceTypeDto} to a {@link SurfaceType} entity.
     *
     * @param dto the DTO to convert
     * @return a SurfaceType entity based on the provided DTO
     */
    public SurfaceType toEntity(SurfaceTypeDto dto) {
        return new SurfaceType(dto.getId(), dto.getName(), dto.getPricePerMinute());
    }
}
