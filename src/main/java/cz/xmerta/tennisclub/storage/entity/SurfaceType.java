package cz.xmerta.tennisclub.storage.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
//@Table(name = "")
public class SurfaceType extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double pricePerMinute;

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
