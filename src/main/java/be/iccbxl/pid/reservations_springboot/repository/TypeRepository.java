package be.iccbxl.pid.reservations_springboot.repository;

import org.springframework.data.repository.CrudRepository;
import be.iccbxl.pid.reservations_springboot.model.Type;
import java.util.Optional;

public interface TypeRepository extends CrudRepository<Type, Long> {
    Type findByType(String type);
    Optional<Type> findById(Long id);
}
