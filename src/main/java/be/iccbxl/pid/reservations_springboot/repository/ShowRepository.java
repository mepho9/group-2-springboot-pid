package be.iccbxl.pid.reservations_springboot.repository;

import java.util.List;

import be.iccbxl.pid.reservations_springboot.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByTitleContainingIgnoreCase(String title);

    List<Show> findAllByOrderByTitleAsc();
    List<Show> findAllByOrderByTitleDesc();


    default Page<Show> getPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }
}