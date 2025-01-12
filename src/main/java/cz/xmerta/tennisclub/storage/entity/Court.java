package cz.xmerta.tennisclub.storage.entity;

import jakarta.persistence.*;


import jakarta.persistence.*;
import java.util.List;

@Entity
//@Table(name = "")
public class Court extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    //@JoinColumn(name = "surface_type_id", nullable = false)
    private SurfaceType surfaceType;

    @Column(nullable = false)
    private int pricePerMinute;

    @OneToMany(mappedBy = "", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    public void setSurfaceType(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
    }

    public int getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(int pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}