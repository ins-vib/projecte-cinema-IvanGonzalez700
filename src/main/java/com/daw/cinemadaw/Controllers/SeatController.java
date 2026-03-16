package com.daw.cinemadaw.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.SeatRepository;

@Controller
public class SeatController {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;

    public SeatController(RoomRepository roomRepository, SeatRepository seatRepository) {
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
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

}
