package be.iccbxl.pid.reservations_springboot.api.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import be.iccbxl.pid.reservations_springboot.api.hateoas.ArtistModelAssembler;
import be.iccbxl.pid.reservations_springboot.model.Artist;
import be.iccbxl.pid.reservations_springboot.repository.ArtistRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class ArtistApiController {

    private final ArtistRepository artistRepository;
    private final ArtistModelAssembler artistAssembler;

    public ArtistApiController(ArtistRepository artistRepository, ArtistModelAssembler artistAssembler) {
        this.artistRepository = artistRepository;
        this.artistAssembler = artistAssembler;
    }

    // GET /api/artists
    @GetMapping("/artists")
    public CollectionModel<EntityModel<Artist>> all() {
        List<EntityModel<Artist>> artists = artistRepository.findAll()
                .stream()
                .map(artistAssembler::toModel)
                .toList();

        return CollectionModel.of(
                artists,
                linkTo(methodOn(ArtistApiController.class).all()).withSelfRel()
        );
    }

    // GET /api/artists/{id}
    @GetMapping("/artists/{id}")
    public ResponseEntity<EntityModel<Artist>> one(@PathVariable Long id) {
        return artistRepository.findById(id)
                .map(artistAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/admin/artists
    @PostMapping("/admin/artists")
    public ResponseEntity<EntityModel<Artist>> create(@RequestBody Artist artist) {
        Artist saved = artistRepository.save(artist);
        EntityModel<Artist> model = artistAssembler.toModel(saved);

        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    // PUT /api/admin/artists/{id}
    @PutMapping("/admin/artists/{id}")
    public ResponseEntity<EntityModel<Artist>> update(@RequestBody Artist artist, @PathVariable Long id) {
        return artistRepository.findById(id)
                .map(existing -> {
                    existing.setFirstname(artist.getFirstname());
                    existing.setLastname(artist.getLastname());

                    Artist saved = artistRepository.save(existing);
                    EntityModel<Artist> model = artistAssembler.toModel(saved);
                    return ResponseEntity.ok(model);
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
