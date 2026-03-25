package be.iccbxl.pid.reservations_springboot.controller;

import java.util.List;

import be.iccbxl.pid.reservations_springboot.model.Reservation;
import be.iccbxl.pid.reservations_springboot.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/admin/reservations")
    public String index(Model model) {
        List<Reservation> reservations = reservationRepository.findAll();

        model.addAttribute("reservations", reservations);
        model.addAttribute("title", "Gestion des réservations");
        model.addAttribute("module", "admin-reservations");

        return "admin/reservation/index";
    }

    @PostMapping("/admin/reservations/{id}/confirm")
    public String confirmReservation(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);

        if (reservation == null) {
            redirectAttributes.addFlashAttribute("error", "Réservation introuvable.");
            return "redirect:/admin/reservations";
        }

        reservation.setStatus("CONFIRMED");
        reservationRepository.save(reservation);

        redirectAttributes.addFlashAttribute("success", "Réservation confirmée avec succès.");
        return "redirect:/admin/reservations";
    }
}