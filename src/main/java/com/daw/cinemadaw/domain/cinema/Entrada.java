package com.daw.cinemadaw.domain.cinema;

import com.daw.cinemadaw.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Screening screening;

    @ManyToOne
    private Seat seat;
    
    @ManyToOne
    private User user;

    public Entrada() {
    }

    public Entrada(Screening screening, Seat seat) {
        this.screening = screening;
        this.seat = seat;
    }
    
    public Entrada(Screening screening, Seat seat, User user) {
        this.screening = screening;
        this.seat = seat;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
