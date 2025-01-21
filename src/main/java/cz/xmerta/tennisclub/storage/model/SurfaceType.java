package cz.xmerta.tennisclub.storage.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "surface_types")
public class SurfaceType extends BaseEntity {

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters.")
    @Column(nullable = false, unique = true)
    private String name;

    @Positive(message = "Price per minute must be a positive value.")
    @Column(nullable = false)
    private double pricePerMinute;
    /**
     * No-argument constructor for Hibernate.
     */
    protected SurfaceType() {}
    /**
     * Argument constructor for testing and other.
     */
    public SurfaceType(Long id, String name, double pricePerMinute) {
        this.setId(id);
        this.name = name;
        this.pricePerMinute = pricePerMinute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(double priceMultiplier) {
        this.pricePerMinute = priceMultiplier;
    }
}
