package com.daw.cinemadaw.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
        // Obtener usuario autenticado
        User usuarioActual = getAuthenticatedUser();
        
        if (usuarioActual == null) {
            return "redirect:/login";
        }

        // Obtener todas las entradas del carrito del usuario (sin ordenar todavía)
        List<Entrada> entradas = entradaRepository.findByUserAndOrderIsNull(usuarioActual);
        
        // Calcular el total
        double total = entradas.stream()
                .mapToDouble(entrada -> entrada.getScreening().getPrice())
                .sum();

        // Pasar datos a la vista
        model.addAttribute("entradas", entradas);
        model.addAttribute("total", total);
        model.addAttribute("usuarioActual", usuarioActual);

        return "session/carrito";
    }

    
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long screeningId,
            @RequestParam Long seatId) {
        
        // Obtener usuario autenticado
        User usuarioActual = getAuthenticatedUser();
        
        if (usuarioActual == null) {
            return "redirect:/login";
        }

        // Obtener la proyección y el asiento
        Optional<Screening> screeningOpt = screeningRepository.findById(screeningId);
        Optional<Seat> seatOpt = seatRepository.findById(seatId);

        if (screeningOpt.isPresent() && seatOpt.isPresent()) {
            // Crear nueva entrada
            Entrada entrada = new Entrada(
                screeningOpt.get(),
                seatOpt.get(),
                usuarioActual
            );

            // Guardar en el carrito
            entradaRepository.save(entrada);
        }

        return "redirect:/carrito";
    }


    @PostMapping("/remove/{entradaId}")
    public String removeFromCart(@PathVariable Long entradaId) {
        // Verificar que la entrada pertenece al usuario actual
        Optional<Entrada> entradaOpt = entradaRepository.findById(entradaId);
        User usuarioActual = getAuthenticatedUser();

        if (entradaOpt.isPresent() && entradaOpt.get().getUser().getId().equals(usuarioActual.getId())) {
            entradaRepository.deleteById(entradaId);
        }

        return "redirect:/carrito";
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
    public String checkoutCart() {
        User usuarioActual = getAuthenticatedUser();
        if (usuarioActual == null) {
            return "redirect:/login";
        }

        List<Entrada> entradasCarrito = entradaRepository.findByUserAndOrderIsNull(usuarioActual);
        if (entradasCarrito.isEmpty()) {
            return "redirect:/carrito";
        }

        Order order = new Order(usuarioActual.getId());
        for (Entrada entrada : entradasCarrito) {
            order.addEntrada(entrada);
        }

        orderRepository.save(order);
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
