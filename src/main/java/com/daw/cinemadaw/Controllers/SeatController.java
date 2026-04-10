package com.daw.cinemadaw.Controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.cinemadaw.domain.cinema.Entrada;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.repository.EntradaRepository;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;
import com.daw.cinemadaw.repository.SeatRepository;

@Controller
public class SeatController {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final ScreeningRepository screeningRepository;
    private final EntradaRepository entradaRepository;

    public SeatController(RoomRepository roomRepository, SeatRepository seatRepository, ScreeningRepository screeningRepository,
            EntradaRepository entradaRepository) {
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
        this.screeningRepository = screeningRepository;
        this.entradaRepository = entradaRepository;
    }

    @GetMapping("/room/{id}")
    public String showSeats(@PathVariable Long id, Model model) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            List<Seat> seats = room.getSeats();
            model.addAttribute("room", room);
            model.addAttribute("seats", seats);
            return "seats/rooms-seats";
        }
        return "redirect:/cinemes";
    }

    @GetMapping("/seats/{id}/edit")
    public String editSeat(@PathVariable Long id, Model model) {
        Optional<Seat> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isPresent()) {
            model.addAttribute("seat", optionalSeat.get());
            return "seats/editar-seats";
        }
        return "redirect:/cinema";
    }

    @PostMapping("/seats/{id}/update")
    public String updateSeat(@PathVariable Long id, @ModelAttribute Seat updatedSeat) {
        Optional<Seat> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isPresent()) {
            Seat seat = optionalSeat.get();
            seat.setNumber(updatedSeat.getNumber());
            seat.setX(updatedSeat.getX());
            seat.setY(updatedSeat.getY());
            seat.setType(updatedSeat.getType());
            seat.setActive(updatedSeat.isActive());
            seatRepository.save(seat);
            return "redirect:/room/" + seat.getRoom().getId();
        }
        return "redirect:/cinema";
    }

    @GetMapping("/seats/{id}/delete")
    public String deleteSeat(@PathVariable Long id) {
        Optional<Seat> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isPresent()) {
            Seat seat = optionalSeat.get();
            Long roomId = seat.getRoom().getId();
            seatRepository.deleteById(id);
            return "redirect:/room/" + roomId;
        }
        return "redirect:/cinema";
    }

    @GetMapping("/seats/room/{roomId}/add")
    public String showCreateSeatForm(@PathVariable Long roomId, Model model) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            model.addAttribute("room", optionalRoom.get());
            model.addAttribute("seat", new Seat());
            return "seats/create-seats";
        }
        return "redirect:/cinema";
    }

    @PostMapping("/seats/room/{roomId}/create")
    public String createSeat(@PathVariable Long roomId, @ModelAttribute Seat newSeat) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            newSeat.setRoom(room);
            seatRepository.save(newSeat);
            return "redirect:/room/" + roomId;
        }
        return "redirect:/cinema";
    }

    @GetMapping("/entrades/{screeningId}")
    public String showEntrades(@PathVariable Long screeningId, Model model) {
        Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);
        if (optionalScreening.isPresent()) {
            Screening screening = optionalScreening.get();
            Room room = screening.getRoom();
            List<Seat> seats = room.getSeats();
            List<Entrada> entradas = entradaRepository.findByScreening(screening);
            Set<Long> entradaSeatIds = new HashSet<>();
            for (Entrada entrada : entradas) {
                entradaSeatIds.add(entrada.getSeat().getId());
            }
            model.addAttribute("room", room);
            model.addAttribute("seats", seats);
            model.addAttribute("screening", screening);
            model.addAttribute("entradaSeatIds", entradaSeatIds);
            return "seats/entrades";
        }
        return "redirect:/movies";
    }

    @PostMapping("/entrades/{screeningId}/comprar")
    public String comprarEntradas(
            @PathVariable Long screeningId,
            @RequestParam(required = false) List<Long> seatIds) {
        Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);
        if (optionalScreening.isPresent() && seatIds != null) {
            Screening screening = optionalScreening.get();
            for (Long seatId : seatIds) {
                Optional<Seat> optionalSeat = seatRepository.findById(seatId);
                if (optionalSeat.isPresent()) {
                    Seat seat = optionalSeat.get();
                    if (seat.getRoom() != null && screening.getRoom() != null
                            && seat.getRoom().getId().equals(screening.getRoom().getId())
                            && !entradaRepository.existsByScreeningAndSeat(screening, seat)) {
                        Entrada entrada = new Entrada(screening, seat);
                        entradaRepository.save(entrada);
                    }
                }
            }
        }
        return "redirect:/entrades/" + screeningId;
    }

}
