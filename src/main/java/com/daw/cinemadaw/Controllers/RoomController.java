package com.daw.cinemadaw.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.domain.cinema.Entrada;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.cinema.SeatType;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.repository.EntradaRepository;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;
import com.daw.cinemadaw.repository.SeatRepository;

@Controller
public class RoomController {

    
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;
    private final SeatRepository seatRepository;
    private final ScreeningRepository screeningRepository;
    private final EntradaRepository entradaRepository;


    public RoomController(RoomRepository roomRepository, CinemaRepository cinemaRepository, 
                          SeatRepository seatRepository, ScreeningRepository screeningRepository,
                          EntradaRepository entradaRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
        this.seatRepository = seatRepository;
        this.screeningRepository = screeningRepository;
        this.entradaRepository = entradaRepository;
    }

    @GetMapping("rooms/create")
    public String create_room(Model model){
        Room room = new Room();    
        model.addAttribute("sala", room);
        return "rooms/create-room";
    }

    @PostMapping("rooms/create")
    public String guardar_room(@ModelAttribute("sala") Room room){
        roomRepository.save(room);
        generateSeatsForRoom(room);
        return "redirect:/movies";
    }

    //detall 
    @GetMapping("rooms/{id}")
    public String show(@PathVariable Long id, Model model){

        Optional<Room>optional = roomRepository.findById(id);
        if(optional.isEmpty()){
            return "redirect:/cinema/";
        }
        Room room=optional.get();
        model.addAttribute("room",room);
        return "rooms/detall-room";
    }

    @GetMapping("rooms/{id}/delete")
    public String delete(@PathVariable Long id){
        Optional<Room>optional =roomRepository.findById(id);
        Long cinemaId=null;
        Room room = null;

        if(optional.isPresent()){
            room= optional.get();
            cinemaId=room.getCinema().getId();

            
            List<Screening> screenings = screeningRepository.findByRoom(room);
            for (Screening screening : screenings) {
                List<Entrada> entradas = entradaRepository.findByScreening(screening);
                entradaRepository.deleteAll(entradas);
            }

           
            screeningRepository.deleteAll(screenings);

            
            List<Seat> seats = room.getSeats();
            for (Seat seat : seats) {
                List<Entrada> entradasSeat = entradaRepository.findBySeat(seat);
                entradaRepository.deleteAll(entradasSeat);
            }


            roomRepository.delete(room);
        }
        return "redirect:/cinemes/"+cinemaId;
    }

    @GetMapping("rooms/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
    Optional<Room> optional = roomRepository.findById(id);
    if (optional.isEmpty()) {
        return "redirect:/cinema/";
        
    }
    model.addAttribute("room",optional.get());
    return "rooms/editar-room";

    }

    @PostMapping("rooms/edit")
    public String update(@ModelAttribute Room room){

    Optional<Room> existingRoom = roomRepository.findById(room.getId());
    if (existingRoom.isEmpty()) {
        return "redirect:/cinema/";
    }

    Room oldRoom = existingRoom.get();
    oldRoom.setName(room.getName());
    oldRoom.setCapacity(room.getCapacity());
    roomRepository.save(oldRoom);
    return "redirect:/cinemes/"+oldRoom.getCinema().getId();

    }

    @GetMapping("rooms/create/{cinemaId}")
    public String create(@PathVariable Long cinemaId, Model model){

        Room room = new Room();
        model.addAttribute("room", room);
        model.addAttribute("cinemaId",cinemaId);

        return "rooms/create-room";

    }


    @PostMapping("rooms/create/{cinemaId}")
    public String create(@PathVariable Long cinemaId, @ModelAttribute Room room){

        Optional<Cinema> cinemaOpt = cinemaRepository.findById(cinemaId);

        if (cinemaOpt.isEmpty()) {
            return "redirect:/cinema/";
        }

        Cinema cinema = (Cinema) cinemaOpt.get();

        room.setCinema(cinema);
        roomRepository.save(room);
        generateSeatsForRoom(room);

        return "redirect:/cinemes/"+cinemaId;
    }

    private void generateSeatsForRoom(Room room) {
        int capacity = room.getCapacity();
        if (capacity <= 0) return;
        int seatsPerRow = 10;
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= capacity; i++) {
            int row = (i - 1) / seatsPerRow;
            int col = (i - 1) % seatsPerRow;
            String rowLetter = String.valueOf((char) ('A' + row));
            Seat seat = new Seat(i, rowLetter, col + 1, row + 1, SeatType.STANDARD);
            seat.setRoom(room);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
    }

}
