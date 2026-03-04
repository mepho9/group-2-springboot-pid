package be.iccbxl.pid.reservations_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorControllerCustom {

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}