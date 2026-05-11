package be.iccbxl.pid.reservations_springboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import be.iccbxl.pid.reservations_springboot.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByTitleContainingIgnoreCase(String title);

    List<Show> findAllByOrderByTitleAsc();
    List<Show> findAllByOrderByTitleDesc();

    List<Show> findByLocation(be.iccbxl.pid.reservations_springboot.model.Location location);


    default Page<Show> getPage(int page, int size) {
        return findAll(PageRequest.of(page, size));
    }
}