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
        

        Cinema cinema1 = new Cinema("Ocine", "Gavarres, 46", "Tarragona", "43122");
        Cinema cinema2 = new Cinema("Yelmo", "Rambla Nova, 10", "Tarragona", "43122");
        Cinema cinema3 = new Cinema("Cinebox", "Rambla Nova, 10", "Tarragona", "43122");

        cinemaRepository.save(cinema1);
        cinemaRepository.save(cinema2);
        cinemaRepository.save(cinema3);

        Room room1 = new Room("Sala 1", 100);
        Room room2 = new Room("Sala 2", 150);
        Room room3 = new Room("Sala 3", 200);

        Room room4 = new Room("Sala 1", 100);
        Room room5 = new Room("Sala 2", 150);
        Room room6 = new Room("Sala 3", 200);

        Room room7 = new Room("Sala 1", 100);
        Room room8 = new Room("Sala 2", 150);
        Room room9 = new Room("Sala 3", 200);

        room1.setCinema(cinema1);
        room2.setCinema(cinema1);
        room3.setCinema(cinema1);

        room4.setCinema(cinema2);
        room5.setCinema(cinema2);
        room6.setCinema(cinema2);

        room7.setCinema(cinema3);
        room8.setCinema(cinema3);
        room9.setCinema(cinema3);

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);
        roomRepository.save(room4);
        roomRepository.save(room5);
        roomRepository.save(room6);
        roomRepository.save(room7);
        roomRepository.save(room8);
        roomRepository.save(room9);

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


        Seat seat1 = new Seat(1, "A", 1, 1, SeatType.STANDARD);
        Seat seat2 = new Seat(2, "A", 2, 1, SeatType.STANDARD);
        Seat seat3 = new Seat(3, "A", 3, 1, SeatType.STANDARD);
        Seat seat4 = new Seat(4, "A", 4, 1, SeatType.STANDARD);
        Seat seat5 = new Seat(5, "A", 5, 1, SeatType.STANDARD);
        Seat seat6 = new Seat(6, "A", 6, 1, SeatType.STANDARD);
        Seat seat7 = new Seat(7, "A", 7, 1, SeatType.STANDARD);
        Seat seat8 = new Seat(8, "A", 8, 1, SeatType.STANDARD);
        Seat seat9 = new Seat(9, "A", 9, 1, SeatType.STANDARD);
        Seat seat10 = new Seat(10, "A", 10, 1, SeatType.STANDARD);

        seat1.setRoom(room1);
        seat2.setRoom(room1);
        seat3.setRoom(room1);
        seat4.setRoom(room1);
        seat5.setRoom(room1);
        seat6.setRoom(room1);
        seat7.setRoom(room1);
        seat8.setRoom(room1);
        seat9.setRoom(room1);
        seat10.setRoom(room1);

        seatRepository.save(seat1);
        seatRepository.save(seat2);
        seatRepository.save(seat3);
        seatRepository.save(seat4);
        seatRepository.save(seat5);
        seatRepository.save(seat6);
        seatRepository.save(seat7);
        seatRepository.save(seat8);
        seatRepository.save(seat9);
        seatRepository.save(seat10);

        /*         
        List<Cinema> llista = cinemaRepository.findAll();

        for (Cinema cinema : llista) {
            System.out.println(cinema);
        }

        Optional<Cinema> optionalCinema = cinemaRepository.findById(4L);

        if (optionalCinema.isPresent()) {
            Cinema cinema = optionalCinema.get();
            System.out.println(cinema);
            cinema.setCity("Reus");
            cinemaRepository.save(cinema);
        } else {
            System.out.println("No trobat");
        }

        List<Cinema> llista2 = cinemaRepository.findByCity("Tarragona");

        for (Cinema cinema : llista2) {
            System.out.println(cinema);
        }

        Cinema cinemaToDelete = llista2.get(0);
        List<Room> roomsToDelete = roomRepository.findByCinema(cinemaToDelete);
        roomRepository.deleteAll(roomsToDelete);
        cinemaRepository.delete(cinemaToDelete);
        */
    }
    
}
