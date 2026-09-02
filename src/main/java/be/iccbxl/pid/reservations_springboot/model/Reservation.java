package be.iccbxl.pid.reservations_springboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "representation_id", nullable = false)
    private Representation representation;

    @Column(nullable = false)
    private Integer places = 1;

    protected Reservation() {}

    public Reservation(User user, Representation representation, Integer places) {
        this.user = user;
        this.representation = representation;
        this.places = places != null ? places : 1;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Representation getRepresentation() {
        return representation;
    }

    public void setRepresentation(Representation representation) {
        this.representation = representation;
    }

    public Integer getPlaces() {
        return places;
    }

    public void setPlaces(Integer places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + id + ", user=" + (user != null ? user.getLogin() : null)
                + ", representation=" + (representation != null ? representation.getId() : null)
                + ", places=" + places + "]";
    }
}
