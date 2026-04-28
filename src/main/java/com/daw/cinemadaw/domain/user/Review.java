package com.daw.cinemadaw.domain.user;

import java.time.LocalDateTime;

import com.daw.cinemadaw.domain.cinema.Movie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table (name = "Reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La puntuació és obligatòria")
    @Min(value = 1, message = "La puntuació mínima és 1")
    @Max(value = 5, message = "La puntuació màxima és 5")
    private Integer rating;

    @Size(max = 500, message = "El comentari no pot superar els 500 caràcters")
    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne
    private Movie movie;

    @NotNull
    @ManyToOne
    private User user;

    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    // ---- Getters i Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
