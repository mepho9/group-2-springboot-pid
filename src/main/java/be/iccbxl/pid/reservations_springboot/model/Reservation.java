package be.iccbxl.pid.reservations_springboot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false, length = 60)
    private String status;

    // Sera utilisé par l'entité d'association representation_reservation
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepresentationReservation> representationReservations = new ArrayList<>();

    protected Reservation() {}

    public Reservation(User user, LocalDateTime bookingDate, String status) {
        this.user = user;
        this.bookingDate = bookingDate;
        this.status = status;
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

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RepresentationReservation> getRepresentationReservations() {
        return representationReservations;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + id + ", user=" + (user != null ? user.getLogin() : null)
                + ", bookingDate=" + bookingDate + ", status=" + status + "]";
    }
}
