package be.iccbxl.pid.reservations_springboot.api.hateoas;

import be.iccbxl.pid.reservations_springboot.api.controller.ArtistApiController;
import be.iccbxl.pid.reservations_springboot.model.Artist;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ArtistModelAssembler implements RepresentationModelAssembler<Artist, EntityModel<Artist>> {

    @Override
    public EntityModel<Artist> toModel(Artist artist) {
        return EntityModel.of(
                artist,
                linkTo(methodOn(ArtistApiController.class).one(artist.getId())).withSelfRel(),
                linkTo(methodOn(ArtistApiController.class).all()).withRel("artists")
        );
    }
}