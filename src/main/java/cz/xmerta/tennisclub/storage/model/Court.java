package cz.xmerta.tennisclub.storage.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "courts")
public class Court extends BaseEntity {

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters.")
    @Column(nullable = false, unique = true)
    private String name;

    @Valid
    @NotNull
    @ManyToOne
    @JoinColumn(name = "surface_type_id", nullable = false)
    private SurfaceType surfaceType;

    /**
     * No-argument constructor for Hibernate.
     */
    protected Court() {}

    /**
     * Argument constructor for testing and other.
     */
    public Court(Long id, String name, SurfaceType surfaceType) {
        this.setId(id);
        this.name = name;
        this.surfaceType = surfaceType;
    }

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

}