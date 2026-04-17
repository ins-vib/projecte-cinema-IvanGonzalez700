package com.daw.cinemadaw.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.DTO.SeatsListDTO;
import com.daw.cinemadaw.domain.cinema.Entrada;
import com.daw.cinemadaw.domain.cinema.Order;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.user.User;
import com.daw.cinemadaw.repository.OrderRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;
import com.daw.cinemadaw.repository.SeatRepository;
import com.daw.cinemadaw.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CurrentController {

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/screenings/seats/{id}")
    public String selectedSeats(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Screening> screeningOpt = screeningRepository.findById(id);
        if (screeningOpt.isEmpty()) {
            return "redirect:/";
        }

        Map<Long, List<Long>> cart = (Map<Long, List<Long>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        SeatsListDTO seatsListDTO = new SeatsListDTO();
        model.addAttribute("selectedSeats", seatsListDTO);
        model.addAttribute("screening", screeningOpt.get());
        return "seats/entrades";
    }

    @PostMapping("/screenings/seats/confirm/{id}")
    public String confirmSeats(@PathVariable Long id, @ModelAttribute SeatsListDTO selectedSeats, Model model, HttpSession session) {
        User usuarioActual = getAuthenticatedUser();
        if (usuarioActual == null) {
            return "redirect:/login";
        }

        Optional<Screening> screeningOpt = screeningRepository.findById(id);
        if (screeningOpt.isEmpty()) {
            return "redirect:/";
        }

        List<Long> seatIds = selectedSeats.getSeats();
        if (seatIds == null || seatIds.isEmpty()) {
            return "redirect:/screenings/seats/" + id;
        }

        Order order = new Order(usuarioActual.getId());
        for (Long seatId : seatIds) {
            Optional<Seat> seatOpt = seatRepository.findById(seatId);
            if (seatOpt.isPresent()) {
                Entrada entrada = new Entrada(screeningOpt.get(), seatOpt.get(), usuarioActual);
                order.addEntrada(entrada);
            }
        }

        orderRepository.save(order);

        Map<Long, List<Long>> cart = (Map<Long, List<Long>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        cart.put(id, seatIds);
        session.setAttribute("cart", cart);

        return "redirect:/";
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userRepository.findByUsername(username).orElse(null);
        }
        return null;
    }
}

    
