package cz.xmerta.tennisclub.storage.model;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;


@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {
    /**
     * No-argument constructor for Hibernate.
     */
    protected Reservation() {}
    /**
     * Argument constructor for testing and other.
     */
    public Reservation(Long id, User user, Court court, LocalDateTime startTime, LocalDateTime endTime, GameType gameType, double price) {
        this.setId(id);
        this.user = user;
        this.court = court;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gameType = gameType;
        this.price = price;
    }

    @Valid
    @NotNull(message = "User cannot be null.")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Valid
    @NotNull(message = "Court cannot be null.")
    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @NotNull(message = "Start time cannot be null.")
    @Column(nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null.")
    @Column(nullable = false)
    private LocalDateTime endTime;

    @NotNull(message = "Game type cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameType gameType;

    @Positive
    @Column(nullable = false)
    private double price;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
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