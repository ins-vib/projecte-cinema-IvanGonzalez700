package com.daw.cinemadaw.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.cinemadaw.domain.cinema.Entrada;
import com.daw.cinemadaw.domain.cinema.Order;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.repository.EntradaRepository;
import com.daw.cinemadaw.repository.OrderRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;
import com.daw.cinemadaw.repository.SeatRepository;

@Service
public class EntradaService {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final EntradaRepository entradaRepository;
    private final OrderRepository orderRepository;

    public EntradaService(ScreeningRepository screeningRepository, SeatRepository seatRepository, EntradaRepository entradaRepository, OrderRepository orderRepository) {
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.entradaRepository = entradaRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order crearOrderTickets(Long userId, Map<Long, List<Long>> cart) {
        Order order = new Order(userId);

        for (Long screeningId : cart.keySet()) {
            Screening screening = screeningRepository.findById(screeningId).orElse(null);
            if (screening == null) {
                throw new IllegalArgumentException("Screening no trobada: " + screeningId);
            }

            List<Long> seatsId = cart.get(screeningId);

            for (Long seatId : seatsId) {
                Seat seat = seatRepository.findById(seatId).orElse(null);
                if (seat == null) {
                    throw new IllegalArgumentException("Seient no trobat: " + seatId);
                }

                if (entradaRepository.existsByScreeningAndSeat(screening, seat)) {
                    throw new IllegalStateException("El seient " + seatId + " ja esta ocupat");
                }

                Entrada entrada = new Entrada(screening, seat);
                order.addEntrada(entrada);
            }
        }

        // La transaccio garanteix que o es guarden order i tickets junts, o es fa rollback.
        return orderRepository.save(order);
    }
}
