package com.daw.cinemadaw.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

     // Mostra la pàgina de login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Pàgina principal
    @GetMapping("/")    
    public String home(Model model) {
    
        // ...

        return "home";
    } 

    // Pàgina d'admin
    @GetMapping("/admin")
    public String admin() {
        return "admin/home";
    }

    // Pàgina de client
    @GetMapping("/client")
    public String client(Model model) {
    
        return "client/home";
    }
}
