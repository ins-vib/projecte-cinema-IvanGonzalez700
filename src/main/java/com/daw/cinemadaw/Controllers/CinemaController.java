package com.daw.cinemadaw.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.DTO.ServicesListDTO;
import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.domain.cinema.Entrada;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.repository.EntradaRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;

import jakarta.validation.Valid;

@Controller
public class CinemaController {

    private final CinemaRepository cinemaRepository;
    private final ScreeningRepository screeningRepository;
    private final EntradaRepository entradaRepository;

    public CinemaController(CinemaRepository cinemaRepository, ScreeningRepository screeningRepository,
                            EntradaRepository entradaRepository) {
        this.cinemaRepository = cinemaRepository;
        this.screeningRepository = screeningRepository;
        this.entradaRepository = entradaRepository;
    }

    @GetMapping("/cinemes")
    public String cinemes(Model model){
        List<Cinema> cinemes = cinemaRepository.findAll();
        model.addAttribute("llista", cinemes);
        return "cinemes/cinemes";
    }

    @GetMapping("/cinemes/user")
    public String cinemasForUser(Model model){
        List<Cinema> cinemes = cinemaRepository.findAll();
        model.addAttribute("llista", cinemes);
        return "cinemes/cinemesUsuari";
    }

    //detalls del cinema
    @GetMapping("/cinemes/{id}")
    public String detall(@PathVariable Long id, Model model) {

    Optional<Cinema> optional = cinemaRepository.findById(id);

    if (optional.isPresent()) {
        Cinema cinema = optional.get();
        model.addAttribute("cinema", cinema);
        return "cinemes/detall-cinema";
        }
        return "redirect:/";
    }

    //Esborrar cinema
    @GetMapping("/cinemes/delete/{id}")
    public String delete(@PathVariable Long id) {

        Optional<Cinema> optional = cinemaRepository.findById(id);

        if (optional.isPresent()) {
            Cinema cinema = optional.get();

            // Per cada sala del cinema, eliminar dependències
            for (Room room : cinema.getRooms()) {
                // 1. Eliminar totes les entrades associades a les sessions d'aquesta sala
                List<Screening> screenings = screeningRepository.findByRoom(room);
                for (Screening screening : screenings) {
                    List<Entrada> entradas = entradaRepository.findByScreening(screening);
                    entradaRepository.deleteAll(entradas);
                }

                // 2. Eliminar totes les sessions d'aquesta sala
                screeningRepository.deleteAll(screenings);

                // 3. Eliminar totes les entrades associades als seients d'aquesta sala
                for (Seat seat : room.getSeats()) {
                    List<Entrada> entradasSeat = entradaRepository.findBySeat(seat);
                    entradaRepository.deleteAll(entradasSeat);
                }
            }

            // 4. Ara ja es pot eliminar el cinema (les sales i seients s'eliminen per cascade)
            cinemaRepository.delete(cinema);
            
        }
        return "redirect:/cinemes";
    }


    //Mostrar el formulari
    @GetMapping("/cinemes/create")
    public String create(Model model) {
        Cinema cinema = new Cinema();
        model.addAttribute("cinema",cinema);
        return "cinemes/create-cinema";
    }

    //Donar de alta el cinema
    @PostMapping("/cinemes/create")
    public String alta(@Valid @ModelAttribute Cinema cinema, BindingResult result) {
        if (result.hasErrors()) {
            return "cinemes/create-cinema";
        }
        cinemaRepository.save(cinema);
        return "redirect:/cinemes";
    }

    @GetMapping("/cinemes/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Optional<Cinema> optional = cinemaRepository.findById(id);
        if (optional.isPresent()) {
            Cinema cinema = optional.get();
            model.addAttribute(cinema);
            return "cinemes/edit-cinema";
        }
        return "redirect:/cinemes";
    }

    @PostMapping("/cinemes/update")
    public String edit(@ModelAttribute Cinema cinema) {
        Optional<Cinema> existingOpt = cinemaRepository.findById(cinema.getId());
        if (existingOpt.isEmpty()) {
            return "redirect:/cinemes";
        }
        //per a que les rooms d'aquest cinema no s'esborrin (si no JPA repository s'enfada)
        Cinema existing = existingOpt.get();
        existing.setName(cinema.getName());
        existing.setAddress(cinema.getAddress());
        existing.setCity(cinema.getCity());
        existing.setPostalCode(cinema.getPostalCode());
        cinemaRepository.save(existing);
        return "redirect:/cinemes";
    }

    @GetMapping("/services")
    public String form(Model model){

        model.addAttribute("allServices", List.of(
        "crispetes",
        "parking",
        "begudes",
        "vip",
        "imax"
        ));

        model.addAttribute("servicesDTO", new ServicesListDTO());
        return "cinemes/services-form";
    }

    @PostMapping("/services")
    public String save(@ModelAttribute ServicesListDTO dto) {

    // Evitar null si no seleccionen res
    if (dto.getServices() == null) {
        dto.setServices(List.of());
    }

    // Mostrar resultats per consola
    System.out.println(dto.getServices());

    return "redirect:/"; 
    }

}
