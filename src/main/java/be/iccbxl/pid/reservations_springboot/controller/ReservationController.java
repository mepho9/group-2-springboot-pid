package be.iccbxl.pid.reservations_springboot.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import be.iccbxl.pid.reservations_springboot.model.Price;
import be.iccbxl.pid.reservations_springboot.model.Representation;
import be.iccbxl.pid.reservations_springboot.model.RepresentationReservation;
import be.iccbxl.pid.reservations_springboot.model.Reservation;
import be.iccbxl.pid.reservations_springboot.model.User;
import be.iccbxl.pid.reservations_springboot.repository.PriceRepository;
import be.iccbxl.pid.reservations_springboot.repository.RepresentationReservationRepository;
import be.iccbxl.pid.reservations_springboot.repository.ReservationRepository;
import be.iccbxl.pid.reservations_springboot.repository.UserRepository;
import be.iccbxl.pid.reservations_springboot.service.RepresentationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReservationController {

    @Autowired
    private RepresentationService representationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RepresentationReservationRepository representationReservationRepository;

    @Autowired
    private PriceRepository priceRepository;

    @GetMapping("/reservations")
    public String index(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByLogin(principal.getName());
        List<Reservation> reservations = reservationRepository.findByUser(user);

        model.addAttribute("reservations", reservations);
        model.addAttribute("title", "Mes réservations");
        model.addAttribute("module", "reservations");

        return "reservation/index";
    }

    @GetMapping("/representations/{id}/reserve")
    public String createReservationForm(@PathVariable("id") Long id, Model model) {
        Representation representation = representationService.get(id);
        if (!representation.getShow().isBookable()) {
            return "redirect:/representations/" + id;
        }

        if (representation == null) {
            return "redirect:/representations";
        }

        java.time.LocalDate today = java.time.LocalDate.now();
        List<Price> prices = new java.util.ArrayList<>();
        prices.addAll(priceRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today));
        prices.addAll(priceRepository.findByStartDateLessThanEqualAndEndDateIsNull(today));
        if (prices.isEmpty()) {
            model.addAttribute("error", "Aucun tarif disponible actuellement.");
        }

        model.addAttribute("representation", representation);
        model.addAttribute("prices", prices);
        model.addAttribute("date", representation.getWhen().toLocalDate());
        model.addAttribute("heure", representation.getWhen().toLocalTime());
        model.addAttribute("title", "Réserver une représentation");

        return "reservation/create";
    }

    @PostMapping("/reservations")
    public String storeReservation(@RequestParam("representationId") Long representationId,
                                   @RequestParam("priceId") Long priceId,
                                   @RequestParam("quantity") Integer quantity,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        Representation representation = representationService.get(representationId);
        if (representation == null || !representation.getShow().isBookable()) {
            redirectAttributes.addFlashAttribute("error", "Cette représentation n'est pas réservable.");
            return "redirect:/representations";
        }
        Price price = priceRepository.findById(priceId).orElse(null);
        User user = userRepository.findByLogin(principal.getName());

        if (representation == null || price == null || user == null || quantity == null || quantity < 1) {
            redirectAttributes.addFlashAttribute("error", "Impossible d'enregistrer la réservation.");
            return "redirect:/representations";
        }

        Reservation reservation = new Reservation(user, LocalDateTime.now(), "PENDING");
        reservationRepository.save(reservation);

        RepresentationReservation rr = new RepresentationReservation(
                representation,
                reservation,
                price,
                quantity
        );
        representationReservationRepository.save(rr);

        redirectAttributes.addFlashAttribute("success", "Réservation enregistrée avec succès.");
        return "redirect:/representations/" + representationId;
    }
    @PostMapping("/reservations/{id}/cancel")
    public String cancelReservation(@PathVariable("id") Long id,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByLogin(principal.getName());
        Reservation reservation = reservationRepository.findById(id).orElse(null);

        if (reservation == null || user == null || !reservation.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Impossible d'annuler cette réservation.");
            return "redirect:/reservations";
        }

        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);

        redirectAttributes.addFlashAttribute("success", "Réservation annulée avec succès.");
        return "redirect:/reservations";
    }
}