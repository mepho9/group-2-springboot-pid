package be.iccbxl.pid.reservations_springboot.api.controller;

import be.iccbxl.pid.reservations_springboot.dto.ReservationDto;
import be.iccbxl.pid.reservations_springboot.model.Representation;
import be.iccbxl.pid.reservations_springboot.model.Reservation;
import be.iccbxl.pid.reservations_springboot.model.Show;
import be.iccbxl.pid.reservations_springboot.model.User;
import be.iccbxl.pid.reservations_springboot.repository.RepresentationRepository;
import be.iccbxl.pid.reservations_springboot.repository.ReservationRepository;
import be.iccbxl.pid.reservations_springboot.service.ShowService;
import be.iccbxl.pid.reservations_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationApiController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RepresentationRepository representationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ShowService showService;

    // GET /api/reservations
    @GetMapping
    public ResponseEntity<List<ReservationDto>> getUserReservations(@RequestParam String userLogin) {
        User user = userService.findByLogin(userLogin);
        if (user == null) {
            user = userService.findByEmail(userLogin);
        }
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Reservation> reservations = reservationRepository.findByUser(user);
        List<ReservationDto> dtos = reservations.stream().map(ReservationDto::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // GET /api/reservations/check
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkUserReservation(@RequestParam String userLogin, @RequestParam long showId) {
        User user = userService.findByLogin(userLogin);
        if (user == null) {
            user = userService.findByEmail(userLogin);
        }
        if (user == null) {
            return ResponseEntity.ok(Map.of("reserved", false));
        }

        boolean reserved = reservationRepository.findByUser(user).stream()
                .anyMatch(r -> r.getRepresentation() != null 
                        && r.getRepresentation().getShow() != null 
                        && r.getRepresentation().getShow().getId().equals(showId));

        return ResponseEntity.ok(Map.of("reserved", reserved));
    }

    // POST /api/reservations
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Map<String, Object> body) {
        String userLogin = (String) body.get("userLogin");
        Long showId = body.get("showId") != null ? Long.parseLong(body.get("showId").toString()) : null;

        User user = userService.findByLogin(userLogin);
        if (user == null) {
            user = userService.findByEmail(userLogin);
        }
        Show show = showService.get(showId);

        if (user == null || show == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Utilisateur ou spectacle introuvable"));
        }

        List<Representation> representations = representationRepository.findByShow(show);
        Representation representation;
        if (!representations.isEmpty()) {
            representation = representations.get(0);
        } else {
            Iterable<Representation> allReps = representationRepository.findAll();
            if (allReps.iterator().hasNext()) {
                representation = allReps.iterator().next();
            } else {
                representation = representationRepository.save(new Representation(show, LocalDateTime.now().plusDays(7), null));
            }
        }

        Reservation reservation = reservationRepository.save(new Reservation(user, representation, 1));
        return ResponseEntity.ok(ReservationDto.fromEntity(reservation));
    }
}
