package cz.xmerta.tennisclub.controller.dto.mapper;

import cz.xmerta.tennisclub.controller.dto.ReservationDto;
import cz.xmerta.tennisclub.service.CourtService;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationDtoMapper {
    public ReservationDto toDTO(Reservation entity) {
        return new ReservationDto(
                entity.getId(),
                entity.getUser(),
                entity.getCourt().getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getGameType(),
                entity.getPrice()
        );
    }

    public Reservation toEntity(ReservationDto dto, CourtService courtService) {
        Court court = courtService.findById(dto.getCourtId())
                .orElseThrow(() -> new IllegalArgumentException("Court ID " + dto.getCourtId() + " does not exist."));


        return new Reservation(
                dto.getId(),
                dto.getUser(),
                court,
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getGameType(),
                dto.getPrice()
        );
    }
}
