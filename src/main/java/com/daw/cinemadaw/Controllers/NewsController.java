package com.daw.cinemadaw.Controllers;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.daw.cinemadaw.domain.cinema.New;
import com.daw.cinemadaw.service.NewsService;

@Controller
public class NewsController {

    @GetMapping("/news")
    public String news(Model model) {
        NewsService newsService = new NewsService();
        ArrayList<New> llista = new ArrayList<>();
        try {
            llista = newsService.GetNews();
        } catch (FileNotFoundException e) {
            //log
        }
        model.addAttribute("llista", llista);
        return "news/news";
    }

}
