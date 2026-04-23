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
            Screening screening = screeningOpt.get();
            Seat seat = seatOpt.get();

            // Comprobar si el usuario ya tiene esta entrada en el carrito
            boolean yaEnCarrito = entradaRepository
                    .existsByScreeningAndSeatAndUserAndOrderIsNull(screening, seat, usuarioActual);

            if (yaEnCarrito) {
                return "redirect:/carrito?error=duplicate";
            }

            // Comprobar si CUALQUIER otro usuario ya tiene este asiento (en carrito o comprado)
            boolean yaReservado = entradaRepository.existsByScreeningAndSeat(screening, seat);

            if (yaReservado) {
                return "redirect:/carrito?error=seat_taken";
            }

            // Crear nueva entrada y guardar en el carrito
            Entrada entrada = new Entrada(screening, seat, usuarioActual);
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
    @org.springframework.transaction.annotation.Transactional
    public String checkoutCart(Model model) {
        User usuarioActual = getAuthenticatedUser();
        if (usuarioActual == null) {
            return "redirect:/login";
        }

        List<Entrada> entradasCarrito = entradaRepository.findByUserAndOrderIsNull(usuarioActual);
        if (entradasCarrito.isEmpty()) {
            return "redirect:/carrito";
        }

        // Check for conflicts: remove entries whose seats were already purchased
        // OR are in another user's cart that was already checked out
        List<Entrada> conflictos = new java.util.ArrayList<>();
        List<Entrada> disponibles = new java.util.ArrayList<>();

        for (Entrada entrada : entradasCarrito) {
            boolean yaPurchased = entradaRepository.existsByScreeningAndSeatAndOrderIsNotNull(
                    entrada.getScreening(), entrada.getSeat());
            boolean enOtroCarrito = entradaRepository.existsByScreeningAndSeatAndUserNotAndOrderIsNull(
                    entrada.getScreening(), entrada.getSeat(), usuarioActual);

            if (yaPurchased) {
                // This seat was already purchased by another user
                conflictos.add(entrada);
            } else {
                // If another user also has it in cart, we still proceed (first to checkout wins)
                disponibles.add(entrada);
            }
        }

        // Remove conflicting entries from the cart
        if (!conflictos.isEmpty()) {
            entradaRepository.deleteAll(conflictos);
        }

        // If all seats had conflicts, redirect with error
        if (disponibles.isEmpty()) {
            return "redirect:/carrito?error=seats_taken";
        }

        // Create the order only with available seats
        Order order = new Order(usuarioActual.getId());
        for (Entrada entrada : disponibles) {
            order.addEntrada(entrada);
        }

        orderRepository.save(order);

        // Delete the same seats from other users' carts (they lost the race)
        for (Entrada entrada : disponibles) {
            List<Entrada> duplicadasOtros = entradaRepository.findByScreeningAndSeatAndUserNotAndOrderIsNull(
                    entrada.getScreening(), entrada.getSeat(), usuarioActual);
            if (!duplicadasOtros.isEmpty()) {
                entradaRepository.deleteAll(duplicadasOtros);
            }
        }

        if (!conflictos.isEmpty()) {
            // Some seats were taken but others were purchased successfully
            model.addAttribute("conflictos", conflictos.size());
        }

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
