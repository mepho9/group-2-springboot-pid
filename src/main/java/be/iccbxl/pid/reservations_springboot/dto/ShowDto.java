package be.iccbxl.pid.reservations_springboot.dto;

import be.iccbxl.pid.reservations_springboot.model.Show;
import lombok.Data;

@Data
public class ShowDto {
    private Long id;
    private String slug;
    private String title;
    private String description;
    private String poster;
    private boolean bookable;
    private double price;

    public static ShowDto fromEntity(Show show) {
        if (show == null) {
            return null;
        }

        ShowDto dto = new ShowDto();
        dto.setId(show.getId());
        dto.setSlug(show.getSlug());
        dto.setTitle(show.getTitle());
        dto.setDescription(show.getDescription());
        dto.setPoster(show.getPosterUrl());
        dto.setBookable(show.isBookable());
        dto.setPrice(show.getPrice());

        return dto;
}
}