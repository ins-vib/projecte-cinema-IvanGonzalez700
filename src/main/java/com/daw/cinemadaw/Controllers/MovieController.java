package com.daw.cinemadaw.Controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.cinemadaw.domain.cinema.Genre;
import com.daw.cinemadaw.domain.cinema.Movie;
import com.daw.cinemadaw.repository.GenreRepository;
import com.daw.cinemadaw.repository.MovieRepository;

import jakarta.validation.Valid;

@Controller
public class MovieController {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public MovieController(MovieRepository movieRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("llista", movieRepository.findAll());
        return "movies/movies";
    }

    @GetMapping("/movies/user")
    public String moviesForUser(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "movies/moviesUsuari";
    }

    @GetMapping("/movies/create")
    public String createMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("allGenres", genreRepository.findAll());
        return "movies/create-movies";
    }

    @PostMapping("/movies/create")
    public String createMovie(@Valid @ModelAttribute Movie movie,
                              BindingResult result,
                              @RequestParam(value = "genreIds", required = false) List<Long> genreIds,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allGenres", genreRepository.findAll());
            return "movies/create-movies";
        }
        assignGenres(movie, genreIds);
        movieRepository.save(movie);
        return "redirect:/movies";
    }

    @GetMapping("/movies/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Optional<Movie> optional = movieRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("movie", optional.get());
            model.addAttribute("allGenres", genreRepository.findAll());
            return "movies/edit-movies";
        }
        return "redirect:/movies";
    }

    @PostMapping("/movies/edit")
    public String editMovie(@Valid @ModelAttribute Movie movie,
                            BindingResult result,
                            @RequestParam(value = "genreIds", required = false) List<Long> genreIds,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allGenres", genreRepository.findAll());
            return "movies/edit-movies";
        }
        Optional<Movie> existing = movieRepository.findById(movie.getId());
        if (existing.isPresent()) {
            Movie m = existing.get();
            m.setTitle(movie.getTitle());
            m.setDuration(movie.getDuration());
            m.setDescription(movie.getDescription());
            m.setReleaseDate(movie.getReleaseDate());
            assignGenres(m, genreIds);
            movieRepository.save(m);
        }
        return "redirect:/movies";
    }

    private void assignGenres(Movie movie, List<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            movie.setGenres(new HashSet<>());
            return;
        }
        Set<Genre> selected = new HashSet<>(genreRepository.findAllById(genreIds));
        movie.setGenres(selected);
    }
}