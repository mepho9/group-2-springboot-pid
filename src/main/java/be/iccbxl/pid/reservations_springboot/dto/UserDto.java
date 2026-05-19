package be.iccbxl.pid.reservations_springboot.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import be.iccbxl.pid.reservations_springboot.model.Role;
import be.iccbxl.pid.reservations_springboot.model.User;

@Data
public class UserDto {
    private Long id;
    private String login;
    private String firstname;
    private String lastname;
    private String email;
    private String langue;
    private LocalDateTime created_at;
    private List<String> roles;

    public static UserDto fromEntity(User user) {
        if (user == null) {
            return null;
        }
        
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setLangue(user.getLangue());
        dto.setCreated_at(user.getCreated_at());
        
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(Role::getRole)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
