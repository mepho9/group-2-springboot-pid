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
import java.security.Principal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import be.iccbxl.pid.reservations_springboot.model.Review;
import be.iccbxl.pid.reservations_springboot.model.User;
import be.iccbxl.pid.reservations_springboot.repository.ReviewRepository;
import be.iccbxl.pid.reservations_springboot.repository.UserRepository;
import java.util.List;
import be.iccbxl.pid.reservations_springboot.model.Review;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ShowController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowService service;

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

        List<Review> reviews = reviewRepository.findByShow(show);

        model.addAttribute("collaborateurs", collaborateurs);
        model.addAttribute("reviews", reviews);
        model.addAttribute("show", show);
        model.addAttribute("title", "Fiche d'un spectacle");
        model.addAttribute("module", "shows");

        return "show/show";
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
    @PostMapping("/shows/{id}/reviews")
    public String storeReview(@PathVariable("id") Long id,
                              @RequestParam("stars") Integer stars,
                              @RequestParam("review") String reviewText,
                              Principal principal,
                              org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        Show show = service.get(id);
        if (show == null) {
            return "redirect:/shows";
        }

        User user = userRepository.findByLogin(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }

        if (stars == null || stars < 1 || stars > 5) {
            redirectAttributes.addFlashAttribute("errorReview", "La note doit être comprise entre 1 et 5.");
            return "redirect:/shows/" + id;
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorReview", "Le commentaire est obligatoire.");
            return "redirect:/shows/" + id;
        }

        if (reviewText.trim().length() < 5) {
            redirectAttributes.addFlashAttribute("errorReview", "Le commentaire doit contenir au moins 5 caractères.");
            return "redirect:/shows/" + id;
        }

        Review review = new Review(user, show, reviewText.trim(), stars);
        reviewRepository.save(review);

        redirectAttributes.addFlashAttribute("successReview", "Votre avis a bien été enregistré.");
        return "redirect:/shows/" + id;
    }
}
