package be.iccbxl.pid.reservations_springboot.dto;

import be.iccbxl.pid.reservations_springboot.model.Review;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class ReviewDto {
    private Long id;
    private String author;
    private String review;
    private Integer stars;
    private String date;

    public static ReviewDto fromEntity(Review reviewEntity) {
        if (reviewEntity == null) {
            return null;
        }
        ReviewDto dto = new ReviewDto();
        dto.setId(reviewEntity.getId());
        if (reviewEntity.getUser() != null) {
            String firstname = reviewEntity.getUser().getFirstname() != null ? reviewEntity.getUser().getFirstname() : "";
            String lastname = reviewEntity.getUser().getLastname() != null ? reviewEntity.getUser().getLastname() : "";
            String fullname = (firstname + " " + lastname).trim();
            dto.setAuthor(fullname.isEmpty() ? reviewEntity.getUser().getLogin() : fullname);
        } else {
            dto.setAuthor("Anonyme");
        }
        dto.setReview(reviewEntity.getReview());
        dto.setStars(reviewEntity.getStars());
        if (reviewEntity.getCreatedAt() != null) {
            dto.setDate(reviewEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        return dto;
    }
}
