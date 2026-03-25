package be.iccbxl.pid.reservations_springboot.controller;

import java.util.List;

import be.iccbxl.pid.reservations_springboot.model.Reservation;
import be.iccbxl.pid.reservations_springboot.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}