package com.daw.cinemadaw.domain.cinema;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El títol de la pel·lícula és obligatori")
    @Size(min = 2, max = 200, message = "El títol ha de tenir entre 2 i 200 caràcters")
    @Column(nullable = false, length = 200)
    private String title;

    @NotNull(message = "La duració és obligatòria")
    @Min(value = 1, message = "La duració ha de ser com a mínim 1 minut")
    @Column(name = "duration_minutes", nullable = false)
    private Integer duration;

    // Calculat automàticament: primer gènere per ordre alfabètic
    @Column(name = "main_genre", length = 50)
    private String mainGenre;

    @NotBlank(message = "La descripció és obligatòria")
    @Size(min = 10, max = 2000, message = "La descripció ha de tenir entre 10 i 2000 caràcters")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "La data d'estrena és obligatòria")
    @Column(name = "release_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "movie_genres",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public Movie() {}

    public Movie(String title, Integer duration, String description, LocalDate releaseDate) {
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    @PrePersist
    @PreUpdate
    private void syncMainGenre() {
        this.mainGenre = genres == null || genres.isEmpty() ? null :
            genres.stream()
                  .map(Genre::getName)
                  .min(Comparator.naturalOrder())
                  .orElse(null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getMainGenre() { return mainGenre; }
    public void setMainGenre(String mainGenre) { this.mainGenre = mainGenre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public Set<Genre> getGenres() { return genres; }
    public void setGenres(Set<Genre> genres) { this.genres = genres; }
}