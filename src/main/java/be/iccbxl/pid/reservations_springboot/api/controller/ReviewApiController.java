package be.iccbxl.pid.reservations_springboot.api.controller;

import be.iccbxl.pid.reservations_springboot.dto.ReviewDto;
import be.iccbxl.pid.reservations_springboot.model.Review;
import be.iccbxl.pid.reservations_springboot.model.Show;
import be.iccbxl.pid.reservations_springboot.model.User;
import be.iccbxl.pid.reservations_springboot.repository.ReviewRepository;
import be.iccbxl.pid.reservations_springboot.service.ShowService;
import be.iccbxl.pid.reservations_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shows/{showId}/reviews")
public class ReviewApiController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ShowService showService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getShowReviews(@PathVariable long showId) {
        Show show = showService.get(showId);
        if (show == null) {
            return ResponseEntity.notFound().build();
        }
        List<Review> reviews = reviewRepository.findByShow(show);
        List<ReviewDto> dtos = reviews.stream().map(ReviewDto::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<?> addReview(@PathVariable long showId, @RequestBody Map<String, Object> body) {
        Show show = showService.get(showId);
        if (show == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Spectacle non trouvé"));
        }

        String userLogin = (String) body.get("userLogin");
        String reviewText = (String) body.get("review");
        Integer stars = body.get("stars") != null ? Integer.parseInt(body.get("stars").toString()) : 5;

        if (userLogin == null || reviewText == null || reviewText.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Données incomplètes"));
        }

        User user = userService.findByLogin(userLogin);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Utilisateur non trouvé"));
        }

        Review review = new Review(user, show, reviewText.trim(), stars);
        reviewRepository.save(review);

        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewDto.fromEntity(review));
    }
}
