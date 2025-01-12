package cz.xmerta.tennisclub.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.util.List;

@Entity
@Table(name = "courts")
public class Court extends BaseEntity {

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters.")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "surface_type_id", nullable = false)
    private SurfaceType surfaceType;

    //to be deleted mby
    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}