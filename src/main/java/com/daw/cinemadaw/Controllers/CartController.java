package com.daw.cinemadaw.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.cinemadaw.domain.cinema.Entrada;
import com.daw.cinemadaw.domain.cinema.Order;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.user.User;
import com.daw.cinemadaw.repository.EntradaRepository;
import com.daw.cinemadaw.repository.OrderRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;
import com.daw.cinemadaw.repository.SeatRepository;
import com.daw.cinemadaw.repository.UserRepository;

@Controller
@RequestMapping("/carrito")
public class CartController {

    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private SeatRepository seatRepository;

    
    @GetMapping
    public String viewCart(Model model) {
        User usuarioActual = getAuthenticatedUser();
        
        if (usuarioActual == null) {
            return "redirect:/login";
        }
        //entradas usuari
        List<Entrada> entradas = entradaRepository.findByUserAndOrderIsNull(usuarioActual);
        //preu
        double total = entradas.stream()
                .mapToDouble(entrada -> entrada.getScreening().getPrice())
                .sum();
        model.addAttribute("entradas", entradas);
        model.addAttribute("total", total);
        model.addAttribute("usuarioActual", usuarioActual);
        if (usuarioActual.getCartNotice() != null && !usuarioActual.getCartNotice().isBlank()) {
            model.addAttribute("cartNotice", usuarioActual.getCartNotice());
            usuarioActual.setCartNotice(null);
            userRepository.save(usuarioActual);
        }
        return "session/carrito";
    }

    @PostMapping("/add")
    @Transactional
    public String addToCart(@RequestParam Long screeningId, @RequestParam Long seatId) {
        User usuarioActual = getAuthenticatedUser();
        if (usuarioActual == null) {
            return "redirect:/login";
        }
        Optional<Screening> screeningOpt = screeningRepository.findById(screeningId);
        if (screeningOpt.isPresent()) {
            Screening screening = screeningOpt.get();
            Seat seat = seatRepository.findByIdForUpdate(seatId);
            if (seat == null) {
                return "redirect:/carrito";
            }

            boolean yaEnCarrito = entradaRepository.existsByScreeningAndSeatAndUserAndOrderIsNull(screening, seat, usuarioActual);
            if (yaEnCarrito) {
                return "redirect:/carrito?error=duplicate";
            }

            boolean yaComprado = entradaRepository.existsByScreeningAndSeatAndOrderIsNotNull(screening, seat);
            if (yaComprado) {
                return "redirect:/carrito?error=seats_taken";
            }

            Entrada entrada = new Entrada(screening, seat, usuarioActual);
            entradaRepository.save(entrada);
        }
        return "redirect:/carrito";
    }


    @PostMapping("/remove/{entradaId}")
    public String removeFromCart(@PathVariable Long entradaId) {
        User usuarioActual = getAuthenticatedUser();
        if (usuarioActual == null) {
            return "redirect:/login";
        }

        Optional<Entrada> entradaOpt = entradaRepository.findById(entradaId);
        if (entradaOpt.isPresent() && entradaOpt.get().getUser().getId().equals(usuarioActual.getId())) {
            entradaRepository.deleteById(entradaId);
            return "redirect:/carrito";
        }
        return "redirect:/carrito?error=removed";
    }

    @PostMapping("/clear")
    public String clearCart() {
        User usuarioActual = getAuthenticatedUser();
        if (usuarioActual != null) {
            List<Entrada> entradasDelUsuario = entradaRepository.findByUserAndOrderIsNull(usuarioActual);
            entradaRepository.deleteAll(entradasDelUsuario);
        }
        return "redirect:/carrito";
    }

    @PostMapping("/checkout")
    @Transactional
    public String checkoutCart(Model model) {

        User usuarioActual = getAuthenticatedUser();
        if (usuarioActual == null) {
            return "redirect:/login";
        }

        List<Entrada> entradasCarrito = entradaRepository.findByUserAndOrderIsNull(usuarioActual);
        if (entradasCarrito.isEmpty()) {
            return "redirect:/carrito";
        }

        List<Long> seatIds = entradasCarrito.stream()
                .map(entrada -> entrada.getSeat().getId())
                .distinct()
                .sorted()
                .toList();
        seatRepository.findAllByIdInForUpdate(seatIds);

        List<Entrada> conflictos = new java.util.ArrayList<>();
        List<Entrada> disponibles = new java.util.ArrayList<>();

        for (Entrada entrada : entradasCarrito) {
            boolean yaPurchased = entradaRepository.existsByScreeningAndSeatAndOrderIsNotNull(entrada.getScreening(), entrada.getSeat());

            if (yaPurchased) {
                conflictos.add(entrada);
            } else {
                disponibles.add(entrada);
            }
        }
    
        if (!conflictos.isEmpty()) {
            entradaRepository.deleteAll(conflictos);
        }

        //error
        if (disponibles.isEmpty()) {
            return "redirect:/carrito?error=seats_taken";
        }

        // Order con entradas que no tengan errores 
        Order order = new Order(usuarioActual.getId());
        for (Entrada entrada : disponibles) {
            order.addEntrada(entrada);
        }

        orderRepository.save(order);

        for (Entrada entrada : disponibles) {
            List<Entrada> duplicadasOtros = entradaRepository.findByScreeningAndSeatAndUserNotAndOrderIsNull(
                    entrada.getScreening(), entrada.getSeat(), usuarioActual);
            if (!duplicadasOtros.isEmpty()) {
                for (Entrada duplicada : duplicadasOtros) {
                    User usuarioAfectado = duplicada.getUser();
                    if (usuarioAfectado != null) {
                        usuarioAfectado.setCartNotice("seats_taken");
                        userRepository.save(usuarioAfectado);
                    }
                }
            }
        }

        if (!conflictos.isEmpty()) {
            model.addAttribute("conflictos", conflictos.size());
        }

        model.addAttribute("entradas", disponibles);
        model.addAttribute("total", order.getTotal());
        model.addAttribute("order", order);

        return "session/confirmed";
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
