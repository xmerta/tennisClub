package cz.xmerta.tennisclub.controller.dto.mapper;

import cz.xmerta.tennisclub.controller.dto.ReservationDto;
import cz.xmerta.tennisclub.service.CourtService;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.Reservation;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link Reservation} entities and {@link ReservationDto} DTOs.
 */
@Component
public class ReservationDtoMapper {

    /**
     * Converts a {@link Reservation} entity to a {@link ReservationDto}.
     *
     * @param entity the Reservation entity to convert
     * @return a DTO representation of the given Reservation entity
     */
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

    /**
     * Converts a {@link ReservationDto} to a {@link Reservation} entity.
     *
     * @param dto the DTO to convert
     * @param courtService the service used to retrieve the {@link Court} by ID
     * @return a Reservation entity based on the provided DTO
     * @throws IllegalArgumentException if the Court ID in the DTO does not exist
     */
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
