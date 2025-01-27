package cz.xmerta.tennisclub.controller.dto.mapper;

import cz.xmerta.tennisclub.controller.dto.CourtDto;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import cz.xmerta.tennisclub.service.SurfaceTypeService;
import org.springframework.stereotype.Component;

@Component
public class CourtDtoMapper {
    public CourtDto toDTO(Court entity) {
        return new CourtDto(entity.getId(), entity.getName(), entity.getSurfaceType().getId());
    }

    public Court toEntity(CourtDto dto, SurfaceTypeService surfaceTypeService) {
        SurfaceType surfaceType = surfaceTypeService.findById(dto.getSurfaceTypeId())
                .orElseThrow(() -> new IllegalArgumentException("SurfaceType ID " + dto.getSurfaceTypeId() + " does not exist."));
        return new Court(dto.getId(), dto.getName(), surfaceType);
    }
}
