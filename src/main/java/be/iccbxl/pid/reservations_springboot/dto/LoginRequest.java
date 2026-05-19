package be.iccbxl.pid.reservations_springboot.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username; // email ou login
    private String password;
}
