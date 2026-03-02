package com.daw.cinemadaw.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.repository.CinemaRepository;

@Controller
public class HomeController {

    private CinemaRepository cinemaRepository;

    public HomeController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/cinemes")
    public String cinemes(Model model){
        List<Cinema> cinemes = cinemaRepository.findAll();
        model.addAttribute("llista", cinemes);
        return "cinemes";
    }

    //detalls del cinema
    @GetMapping("/cinema/{id}")
    public String detall(@PathVariable Long id, Model model) {

    Optional<Cinema> optional = cinemaRepository.findById(id);

    if (optional.isPresent()) {
        Cinema cinema = optional.get();
        model.addAttribute("cinema", cinema);
        return "detall-cinema";
    }

        return "redirect:/";

    }

    //Esborrar cinema
    @GetMapping("/cinema/delete/{id}")
    public void esborrar(@PathVariable Long id, Model model) {

        cinemaRepository.deleteById(id);

    }
}
