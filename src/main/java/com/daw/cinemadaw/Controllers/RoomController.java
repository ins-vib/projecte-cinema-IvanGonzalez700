package com.daw.cinemadaw.Controllers;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.repository.RoomRepository;

@Controller
public class RoomController {

    
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;


    public RoomController(RoomRepository roomRepository, CinemaRepository cinemaRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping("rooms/create")
    public String create_room(Model model){

    Room room = new Room();    
    model.addAttribute("sala", room);
    return "rooms/create-room";
    }

    @PostMapping("rooms/create")
    public String guardarroom(@ModelAttribute("sala") Room room){
        roomRepository.save(room);
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

    // delete
    @GetMapping("rooms/{id}/delete")
    public String delete(@PathVariable Long id){
        Optional<Room>optional =roomRepository.findById(id);
        Long cinemaId=null;
        Room room =null;

        if(optional.isPresent()){
            room= optional.get();
            cinemaId=room.getCinema().getId();
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
    return "redirect:/cinema/"+oldRoom.getCinema().getId();

    }

    @GetMapping("rooms/create/{cinemaId}")
    public String create(@PathVariable Long cinemaId, Model model){

        Room room = new Room();
        model.addAttribute("room", room);
        model.addAttribute("cinemaId",cinemaId);

        return "rooms/create";

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

        return "redirect:/cinema/"+cinemaId;
    }

}
