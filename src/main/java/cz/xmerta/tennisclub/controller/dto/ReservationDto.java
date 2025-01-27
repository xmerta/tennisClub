package cz.xmerta.tennisclub.controller.dto;

import cz.xmerta.tennisclub.storage.model.GameType;

import cz.xmerta.tennisclub.storage.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;

    @NotNull(message = "User cannot be null.")
    private User user;

    @NotNull(message = "Court ID cannot be null.")
    private Long courtId;

    @NotNull(message = "Start time cannot be null.")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null.")
    private LocalDateTime endTime;

    @NotNull(message = "Game type cannot be null.")
    private GameType gameType;

    @Positive(message = "Price must be positive.")
    private double price;

    public ReservationDto(Long id, User user, Long courtId, LocalDateTime startTime, LocalDateTime endTime, GameType gameType, double price) {
        this.id = id;
        this.user = user;
        this.courtId = courtId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gameType = gameType;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
