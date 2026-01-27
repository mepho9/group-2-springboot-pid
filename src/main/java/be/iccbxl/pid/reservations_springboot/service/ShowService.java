package be.iccbxl.pid.reservations_springboot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import be.iccbxl.pid.reservations_springboot.model.Location;
import be.iccbxl.pid.reservations_springboot.model.Show;
import be.iccbxl.pid.reservations_springboot.repository.ShowRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowService {

    @Autowired
    private ShowRepository repository;

    public List<Show> getAll() {
        List<Show> shows = new ArrayList<>();
        repository.findAll().forEach(shows::add);
        return shows;
    }

    public Show get(Long id) {
        Optional<Show> show = repository.findById(id);
        return show.orElse(null);
    }

    public void add(Show show) {
        repository.save(show);
    }

    public void update(Long id, Show show) {
        // MVP: on sauvegarde tel quel (l'id est porté par l'entité)
        repository.save(show);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Show> getFromLocation(Location location) {
        return repository.findByLocation(location);
    }
}
