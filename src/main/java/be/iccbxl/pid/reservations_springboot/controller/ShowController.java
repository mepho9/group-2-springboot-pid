package be.iccbxl.pid.reservations_springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import be.iccbxl.pid.reservations_springboot.model.Artist;
import be.iccbxl.pid.reservations_springboot.model.ArtistType;
import be.iccbxl.pid.reservations_springboot.model.Show;
import be.iccbxl.pid.reservations_springboot.service.ShowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShowController {

    @Autowired
    private ShowService service;

    @GetMapping("/shows")
    public String index(Model model) {
        List<Show> shows = service.getAll();

        model.addAttribute("shows", shows);
        model.addAttribute("title", "Liste des spectacles");
        model.addAttribute("module", "shows");

        return "show/index";
    }

    @GetMapping("/shows/{id}")
    public String show(Model model, @PathVariable("id") Long id) {
        Show show = service.get(id);

        if (show == null) {
            return "redirect:/shows";
        }

        Map<String, ArrayList<Artist>> collaborateurs = new TreeMap<>();

        for (ArtistType at : show.getArtistTypes()) {
            String type = at.getType().getType();

            collaborateurs.computeIfAbsent(type, k -> new ArrayList<>());
            collaborateurs.get(type).add(at.getArtist());
        }

        model.addAttribute("collaborateurs", collaborateurs);
        model.addAttribute("show", show);
        model.addAttribute("title", "Fiche d'un spectacle");
        model.addAttribute("module", "shows");

        return "show/show";
    }
}
