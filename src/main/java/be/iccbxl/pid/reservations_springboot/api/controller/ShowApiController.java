package be.iccbxl.pid.reservations_springboot.api.controller;

import be.iccbxl.pid.reservations_springboot.dto.ShowDto;
import be.iccbxl.pid.reservations_springboot.model.Show;
import be.iccbxl.pid.reservations_springboot.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowApiController {

    @Autowired
    private ShowService showService;

    // GET /api/shows
    @GetMapping
    public ResponseEntity<List<ShowDto>> getAllShows(){
        List<Show> shows = showService.getAll();
        List<ShowDto> showDtos = shows.stream().map(ShowDto::fromEntity).toList();
        return ResponseEntity.ok(showDtos);
    }

    // GET /api/shows/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowById(@PathVariable long id) {
        Show show = showService.get(id);
        if (show == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ShowDto.fromEntity(show));
    }
}
