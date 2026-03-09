package com.daw.cinemadaw.Controllers;


import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.daw.cinemadaw.domain.cinema.New;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.service.NewsService;

@Controller
public class HomeController {

    private CinemaRepository cinemaRepository;

    public HomeController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping("/")
    public String home(Model model) {

        NewsService newsService = new NewsService();
        ArrayList<New> llista = new ArrayList<>();
        try {
            llista = newsService.GetNews();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        model.addAttribute("llista",llista);
        return "home";
    }

}
