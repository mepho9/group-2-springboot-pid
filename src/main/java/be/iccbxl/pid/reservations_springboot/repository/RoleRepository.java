package be.iccbxl.pid.reservations_springboot.repository;

import org.springframework.data.repository.CrudRepository;
import be.iccbxl.pid.reservations_springboot.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);
}
