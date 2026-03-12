package com.daw.cinemadaw.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.repository.CinemaRepository;

import jakarta.validation.Valid;

@Controller
public class CinemaController {

    private CinemaRepository cinemaRepository;

    public CinemaController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping("/cinemes")
    public String cinemes(Model model){
        List<Cinema> cinemes = cinemaRepository.findAll();
        model.addAttribute("llista", cinemes);
        return "cinemes";
    }

    //detalls del cinema
    @GetMapping("/cinemes/{id}")
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
    @GetMapping("/cinemes/delete/{id}")
    public String delete(@PathVariable Long id) {

        Optional<Cinema> optional = cinemaRepository.findById(id);

        if (optional.isPresent()) {
            Cinema cinema = optional.get();
            cinemaRepository.delete(cinema);
            
        }
        return "redirect:/cinemes";
    }


    //Mostrar el formulari
    @GetMapping("/cinemes/create")
    public String create(Model model) {
        Cinema cinema = new Cinema();
        model.addAttribute("cinema",cinema);
        return "create-cinema";
    }

    //Donar de alta el cinema
    @PostMapping("/cinemes/create")
    public String alta(@Valid @ModelAttribute Cinema cinema, BindingResult result) {

        if (result.hasErrors()) {
            return "create-cinema";
        }
        cinemaRepository.save(cinema);
        return "redirect:/cinemes";
    }

    @GetMapping("/cinemes/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<Cinema> optional = cinemaRepository.findById(id);
        if (optional.isPresent()) {
            Cinema cinema = optional.get();
            model.addAttribute(cinema);
            return "edit-cinema";
        }
        return "redirect:/cinemes";
    }

    @PostMapping("/cinemes/update")
    public String edit(@ModelAttribute Cinema cinema) {
        cinemaRepository.save(cinema);
        return "redirect:/cinemes";
    }

}
