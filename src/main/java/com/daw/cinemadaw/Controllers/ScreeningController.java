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
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.repository.MovieRepository;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;

@Controller
public class ScreeningController {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public ScreeningController(ScreeningRepository screeningRepository, MovieRepository movieRepository, RoomRepository roomRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }


    @GetMapping("/screenings/{movieId}/screenings")
    public String screenings(@PathVariable Long movieId, Model model) {
        Optional<Movie> optional = movieRepository.findById(movieId);
        if (optional.isPresent()) {
            Movie movie = optional.get();
            List<Screening> screenings = screeningRepository.findByMovie(movie);
            model.addAttribute("movie", movie);
            model.addAttribute("screenings", screenings);
            return "screenings/screenings";
        }
        return "redirect:/movies";
    }


    @GetMapping("/screenings/{movieId}/screenings/create")
    public String createScreeningForm(@PathVariable Long movieId, Model model) {
        Optional<Movie> optional = movieRepository.findById(movieId);
        if (optional.isPresent()) {
            Movie movie = optional.get();
            Screening screening = new Screening();
            screening.setMovie(movie);
            List<Room> rooms = roomRepository.findAll();
            model.addAttribute("screening", screening);
            model.addAttribute("movie", movie);
            model.addAttribute("rooms", rooms);
            return "screenings/create-screening";
        }
        return "redirect:/movies";
    }


    @PostMapping("/screenings/{movieId}/screenings/create")
    public String createScreening(@PathVariable Long movieId, @ModelAttribute Screening screening) {
        Optional<Movie> optional = movieRepository.findById(movieId);
        if (optional.isPresent()) {
            screening.setMovie(optional.get());
            screeningRepository.save(screening);
        }
        return "redirect:/screenings/" + movieId + "/screenings";
    }


    @GetMapping("/screenings/{movieId}/screenings/edit/{screeningId}")
    public String editScreeningForm(@PathVariable Long movieId, @PathVariable Long screeningId, Model model) {
        Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        if (optionalScreening.isPresent() && optionalMovie.isPresent()) {
            List<Room> rooms = roomRepository.findAll();
            model.addAttribute("screening", optionalScreening.get());
            model.addAttribute("movie", optionalMovie.get());
            model.addAttribute("rooms", rooms);
            return "screenings/edit-screening";
        }
        return "redirect:/screenings/" + movieId + "/screenings";
    }


    @PostMapping("/screenings/{movieId}/screenings/edit")
    public String editScreening(@PathVariable Long movieId, @ModelAttribute Screening screening) {
        Optional<Movie> optional = movieRepository.findById(movieId);
        if (optional.isPresent()) {
            screening.setMovie(optional.get());
            screeningRepository.save(screening);
        }
        return "redirect:/screenings/" + movieId + "/screenings";
    }


    @GetMapping("/screenings/{movieId}/screenings/delete/{screeningId}")
    public String deleteScreening(@PathVariable Long movieId, @PathVariable Long screeningId) {
        Optional<Screening> optional = screeningRepository.findById(screeningId);
        if (optional.isPresent()) {
            screeningRepository.delete(optional.get());
        }
        return "redirect:/screenings/" + movieId + "/screenings";
    }

        @GetMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        Optional<Movie> optional = movieRepository.findById(id);
        if (optional.isPresent()) {
            // Primer eliminar totes les projeccions associades
            List<Screening> screenings = screeningRepository.findByMovie(optional.get());
            screeningRepository.deleteAll(screenings);
            movieRepository.delete(optional.get());
        }
        return "redirect:/movies";
    }
}
