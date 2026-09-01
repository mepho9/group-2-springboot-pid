package be.iccbxl.pid.reservations_springboot.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import be.iccbxl.pid.reservations_springboot.dto.UserDto;
import be.iccbxl.pid.reservations_springboot.dto.UserProfileDto;
import be.iccbxl.pid.reservations_springboot.dto.UserRegistrationDto;
import be.iccbxl.pid.reservations_springboot.model.User;
import be.iccbxl.pid.reservations_springboot.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserService userService;

    // GET /api/users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    // GET /api/users/{login}
    @GetMapping("/{login}")
    public ResponseEntity<UserDto> getUserByLogin(@PathVariable String login) {
        User user = userService.findByLogin(login);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserDto.fromEntity(user));
    }

    // PUT /api/users/{login}
    @PutMapping("/{login}")
    public ResponseEntity<?> updateUserProfile(@PathVariable String login, @Valid @RequestBody UserProfileDto profileDto) {
        User user = userService.findByLogin(login);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        // S'assurer de mettre à jour le bon utilisateur
        profileDto.setId(user.getId());
        
        try {
            userService.updateUserFromDto(profileDto);
            return ResponseEntity.ok(Map.of("message", "Profil mis à jour avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erreur lors de la mise à jour : " + e.getMessage()));
        }
    }

    // POST /api/users/register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        if (!userService.isLoginAndEmailAvailable(registrationDto.getLogin(), registrationDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Login ou Email déjà utilisé"));
        }

        try {
            userService.registerFromDto(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Utilisateur enregistré avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erreur lors de l'inscription : " + e.getMessage()));
        }
    }

    // DELETE /api/users/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé avec succès"));
    }

    // DELETE /api/users/{login}
    @DeleteMapping("/{login}")
    public ResponseEntity<?> deleteUserByLogin(@PathVariable String login) {
        User user = userService.findByLogin(login);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteByLogin(login);
        return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé avec succès"));
    }

    // Gère les erreurs de validation (ex: @NotBlank, @Size)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
