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
@Table(name = "representation_reservation")
public class RepresentationReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "representation_id", nullable = false)
    private Representation representation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "price_id", nullable = false)
    private Price price;

    @Column(nullable = false)
    private Integer quantity;

    protected RepresentationReservation() {}

    public RepresentationReservation(Representation representation,
                                     Reservation reservation,
                                     Price price,
                                     Integer quantity) {
        this.representation = representation;
        this.reservation = reservation;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Representation getRepresentation() {
        return representation;
    }

    public void setRepresentation(Representation representation) {
        this.representation = representation;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "RepresentationReservation [id=" + id
                + ", representation=" + (representation != null ? representation.getId() : null)
                + ", reservation=" + (reservation != null ? reservation.getId() : null)
                + ", price=" + (price != null ? price.getId() : null)
                + ", quantity=" + quantity + "]";
    }
}
