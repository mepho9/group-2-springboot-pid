package be.iccbxl.pid.reservations_springboot.controller;

import java.util.List;

import be.iccbxl.pid.reservations_springboot.model.Review;
import be.iccbxl.pid.reservations_springboot.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/admin/reviews")
    public String index(Model model) {
        List<Review> reviews = reviewRepository.findByValidatedFalse();

        model.addAttribute("reviews", reviews);
        model.addAttribute("title", "Avis à valider");
        model.addAttribute("module", "admin-reviews");

        return "admin/review/index";
    }

    @PostMapping("/admin/reviews/{id}/validate")
    public String validateReview(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Review review = reviewRepository.findById(id).orElse(null);

        if (review == null) {
            redirectAttributes.addFlashAttribute("error", "Avis introuvable.");
            return "redirect:/admin/reviews";
        }

        review.setValidated(true);
        reviewRepository.save(review);

        redirectAttributes.addFlashAttribute("success", "Avis validé avec succès.");
        return "redirect:/admin/reviews";
    }
}