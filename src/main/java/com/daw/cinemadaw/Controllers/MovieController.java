package com.daw.cinemadaw.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.domain.cinema.Movie;
import com.daw.cinemadaw.repository.MovieRepository;


@Controller
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movies")
    public String movies(Model model) {
        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("llista", movies);
        return "movies/movies";
    }

    
    @GetMapping("/movies/user")
    public String moviesForUser(Model model) {
        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
        return "movies/moviesUsuari";
    }

        @GetMapping("/movies/create")
    public String createMovieForm(Model model) {
        Movie movie = new Movie();
        model.addAttribute("movie", movie);
        return "movies/create-movies";
    }


    @PostMapping("/movies/create")
    public String createMovie(@ModelAttribute Movie movie) {
        movieRepository.save(movie);
        return "redirect:/movies";
    }

    @GetMapping("/movies/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Optional<Movie> optional = movieRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("movie", optional.get());
            return "movies/edit-movies";
        }
        return "redirect:/movies";
    }

    @PostMapping("/movies/edit")
    public String editMovie(@ModelAttribute Movie movie) {
        movieRepository.save(movie);
        return "redirect:/movies";
    }
}
