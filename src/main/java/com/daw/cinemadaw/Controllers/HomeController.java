package com.daw.cinemadaw.Controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.daw.cinemadaw.domain.cinema.Movie;
import com.daw.cinemadaw.repository.MovieRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;

@Controller
public class HomeController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @GetMapping("/login")
    public String login() {
        return "homelogin/login";
    }

    @GetMapping("/logout-success")
    public String logout() {
        return "homelogin/logout";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "homelogin/home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/home";
    }

    @GetMapping("/client")
    public String client(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "client/home";
    }

    @GetMapping("/client/movie/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        model.addAttribute("movie", movie);
        model.addAttribute("screenings", screeningRepository
                .findByMovieAndScreeningDateTimeGreaterThanEqualOrderByScreeningDateTimeAsc(movie, LocalDateTime.now()));
        return "client/movie-detail";
    }
}
