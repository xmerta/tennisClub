package cz.xmerta.tennisclub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class SurfaceTypeDto {

    private Long id;

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters.")
    private String name;

    @Positive(message = "Price per minute must be a positive value.")
    private double pricePerMinute;

    public SurfaceTypeDto(Long id, String name, double pricePerMinute) {
        this.id = id;
        this.name = name;
        this.pricePerMinute = pricePerMinute;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setPricePerMinute(double pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }
}
