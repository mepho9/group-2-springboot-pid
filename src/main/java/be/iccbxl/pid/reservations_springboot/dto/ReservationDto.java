package be.iccbxl.pid.reservations_springboot.dto;

import be.iccbxl.pid.reservations_springboot.model.Reservation;
import lombok.Data;

@Data
public class ReservationDto {
    private Long id;
    private Long showId;
    private String showTitle;
    private String showPoster;
    private double showPrice;
    private int places;

    public static ReservationDto fromEntity(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setPlaces(reservation.getPlaces() != null ? reservation.getPlaces() : 1);

        if (reservation.getRepresentation() != null && reservation.getRepresentation().getShow() != null) {
            dto.setShowId(reservation.getRepresentation().getShow().getId());
            dto.setShowTitle(reservation.getRepresentation().getShow().getTitle());
            dto.setShowPoster(reservation.getRepresentation().getShow().getPosterUrl());
            dto.setShowPrice(reservation.getRepresentation().getShow().getPrice());
        }

        return dto;
    }
}
