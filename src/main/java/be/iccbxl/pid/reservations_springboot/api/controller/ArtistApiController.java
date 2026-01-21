package be.iccbxl.pid.reservations_springboot.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import be.iccbxl.pid.reservations_springboot.model.Artist;
import be.iccbxl.pid.reservations_springboot.repository.ArtistRepository;

@RestController
@RequestMapping("/api")
public class ArtistApiController {

    private final ArtistRepository artistRepository;

    public ArtistApiController(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    // GET /api/artists
    @GetMapping("/artists")
    public List<Artist> all() {
        return artistRepository.findAll();
    }

    // GET /api/artists/{id}
    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> one(@PathVariable Long id) {
        return artistRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/admin/artists
    @PostMapping("/admin/artists")
    public ResponseEntity<Artist> create(@RequestBody Artist artist) {
        Artist saved = artistRepository.save(artist);
        return ResponseEntity
                .created(URI.create("/api/artists/" + saved.getId()))
                .body(saved);
    }

    // PUT /api/admin/artists/{id}
    @PutMapping("/admin/artists/{id}")
    public ResponseEntity<Artist> update(@RequestBody Artist artist, @PathVariable Long id) {
        return artistRepository.findById(id)
                .map(existing -> {
                    // adapte les champs selon ton modèle Artist
                    existing.setFirstname(artist.getFirstname());
                    existing.setLastname(artist.getLastname());

                    Artist saved = artistRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/admin/artists/{id}
    @DeleteMapping("/admin/artists/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!artistRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        artistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}