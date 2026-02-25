package com.daw.cinemadaw.config;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.cinema.SeatType;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.SeatRepository;

import jakarta.transaction.Transactional;

@Component
public class Proves implements CommandLineRunner {

    private CinemaRepository cinemaRepository;
    private RoomRepository roomRepository;
    private SeatRepository seatRepository;

    
    public Proves(CinemaRepository cinemaRepository, RoomRepository roomRepository, SeatRepository seatRepository) {
        this.cinemaRepository = cinemaRepository;
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
    }


    @Transactional
    @Override
    public void run(String... args) throws Exception {
        

        Optional<Cinema> optionalCinema = cinemaRepository.findById(1L);

        if (optionalCinema.isPresent()) {
            Cinema cinema = optionalCinema.get();
            List<Room> rooms = cinema.getRooms();
            for (Room room : rooms) {
                System.out.println(room.getName());
            }
            
        } else {
            System.out.println("No trobat");
        }

        Cinema cinema3D = new Cinema("Cinema 3D", "Gavarres, 46", "Tarragona", "43122");
        Room room3d1 = new Room("Sala 3D 1", 500);
        room3d1.setCinema(cinema3D);
        cinema3D.getRooms().add(room3d1);
        cinemaRepository.save(cinema3D);
        
        List<Room> llista = roomRepository.findAll();

        for (Room room : llista) {
            System.out.println(room);
                for(int i = 1; i < 10; i++){
                    for(int j = 0; j <10; j++){
                        Seat seat = new Seat(i,j+"",i, j, SeatType.STANDARD);
                        seat.setRoom(room);
                        seatRepository.save(seat);
                    }
                }
        }


        List<Cinema> llista2 = cinemaRepository.findByCity("Tarragona");

        for (Cinema cinema : llista2) {
            System.out.println(cinema);
        }

        Cinema cinemaToDelete = llista2.get(0);
        List<Room> roomsToDelete = roomRepository.findByCinema(cinemaToDelete);
        roomRepository.deleteAll(roomsToDelete);
        cinemaRepository.delete(cinemaToDelete);


        Optional<Cinema> optionalC = cinemaRepository.findById(1L);
        if (optionalC.isPresent()) {
            Cinema c = optionalC.get();
            System.out.println(c);

            List<Room> sales = c.getRooms();
            for (Room r : sales) {
                System.out.println(r);
                List<Seat> seients = r.getSeats();
                
                for (Seat s : seients) {
                    System.out.println(s);
                }
            }            
        } 
    }
}
