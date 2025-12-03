package be.iccbxl.pid.reservations_springboot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import be.iccbxl.pid.reservations_springboot.dto.UserProfileDto;
import be.iccbxl.pid.reservations_springboot.dto.UserRegistrationDto;
import be.iccbxl.pid.reservations_springboot.model.User;
import be.iccbxl.pid.reservations_springboot.model.UserRole;
import be.iccbxl.pid.reservations_springboot.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	public boolean isLoginAndEmailAvailable(String login, String email) {
		return !userRepository.existsByLogin(login) && !userRepository.existsByEmail(email);
	}

	public void registerFromDto(UserRegistrationDto dto) {
		User user = new User();
		user.setFirstname(dto.getFirstname());
		user.setLastname(dto.getLastname());
		user.setLogin(dto.getLogin());
		user.setEmail(dto.getEmail());
		user.setLangue(dto.getLangue());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(UserRole.MEMBER);
		userRepository.save(user);
	}

	public void updateUserFromDto(UserProfileDto dto) {
		User user = userRepository.findById(dto.getId())
				.orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

		user.setFirstname(dto.getFirstname());
		user.setLastname(dto.getLastname());
		user.setEmail(dto.getEmail());
		user.setLangue(dto.getLangue());

		// Si un nouveau mot de passe est fourni et validé
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		userRepository.save(user);
	}

	public void deleteUser(long id) {
		userRepository.deleteById(id);
	}


	public void deleteByLogin(String login) {
		userRepository.findByLogin(login).ifPresent(user -> {
			user.setDeletedAt(LocalDateTime.now());
			userRepository.save(user);
		});
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();

		repository.findAll().forEach(users::add);

		return users;
	}

	public User getUser(String id) {
		int indice = Integer.parseInt(id);

		return repository.findById(indice);
	}

	public void addUser(User user) {
		repository.save(user);
	}

	public void updateUser(String id, User user) {
		repository.save(user);
	}

	public void deleteUser(String id) {
		Long indice = (long) Integer.parseInt(id);

		repository.deleteById(indice);
	}


}
