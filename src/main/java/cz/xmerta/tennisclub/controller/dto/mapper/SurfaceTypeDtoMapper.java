package cz.xmerta.tennisclub.controller.dto.mapper;

import cz.xmerta.tennisclub.controller.dto.SurfaceTypeDto;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import org.springframework.stereotype.Component;

@Component
public class SurfaceTypeDtoMapper {

    public SurfaceTypeDto toDTO(SurfaceType entity) {
        return new SurfaceTypeDto(entity.getId(),entity.getName(), entity.getPricePerMinute());
    }

    public SurfaceType toEntity(SurfaceTypeDto dto) {
        return new SurfaceType(dto.getId(), dto.getName(), dto.getPricePerMinute());
    }
}
