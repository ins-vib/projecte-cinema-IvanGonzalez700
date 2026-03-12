package com.daw.cinemadaw.domain.cinema;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="el titol de la peli")
    @Size(min = 2, max = 100, message = "la ciutat")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message="la duracio")
    @Column(name = "duration_minutes", nullable = false)
    private int duration;

    @NotBlank(message="el genere")
    @Size(min = 2, max = 100, message = "el genere")
    @Column(length = 50)
    private String genre;

    @NotBlank(message="la descripcio")
    @Size(min = 2, max = 100, message = "la descripcio")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message="la data de sortida")
    @Column(name = "release_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate releaseDate;

    public Movie(String title, int duration, String genre, String description, LocalDate releaseDate) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    public Movie() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
