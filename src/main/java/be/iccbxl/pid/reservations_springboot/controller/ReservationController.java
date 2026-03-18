package be.iccbxl.pid.reservations_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {

    @GetMapping("/reservation-placeholder")
    public String reservationPlaceholder() {
        return "reservation/placeholder";
    }
}