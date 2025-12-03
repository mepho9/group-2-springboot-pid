package be.iccbxl.pid.reservations_springboot.repository;

import java.util.List;

import be.iccbxl.pid.reservations_springboot.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findByLastname(String lastname);

	User findById(long id);
	User findByLogin(String login);
	User findByEmail(String email);
}
