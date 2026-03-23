package com.daw.cinemadaw.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.cinema.SeatType;
import com.daw.cinemadaw.domain.user.Role;
import com.daw.cinemadaw.domain.user.User;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.SeatRepository;
import com.daw.cinemadaw.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class Proves implements CommandLineRunner {

    private CinemaRepository cinemaRepository;
    private RoomRepository roomRepository;
    private SeatRepository seatRepository;
    private UserRepository userRepository;
    BCryptPasswordEncoder encoder;
    
    public Proves(CinemaRepository cinemaRepository, RoomRepository roomRepository, SeatRepository seatRepository, UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.cinemaRepository = cinemaRepository;
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }


    @Transactional
    @Override
    public void run(String... args) throws Exception {
        
        List<Cinema> cinemas = cinemaRepository.findAll();
        for (Cinema cinema : cinemas) {
            List<Room> rooms = cinema.getRooms();
            for (Room room : rooms) {
                int number = 1;
                for (int y = 0; y < 10; y++) {
                    String row = String.valueOf((char) ('A' + y)); 
                    for (int x = 0; x < 10; x++) {
                        Seat seat = new Seat(number, row, x, y, SeatType.STANDARD);
                        seat.setRoom(room);
                        seatRepository.save(seat);
                        number++;
                    }
                }
            }
        }

    User admin = new User();
    admin.setUsername("admin");
    admin.setPassword(encoder.encode("1234"));
    admin.setRole(Role.ADMIN);
    userRepository.save(admin);

    User client = new User();
    client.setUsername("client");
    client.setPassword(encoder.encode("1234"));
    client.setRole(Role.CLIENT);
    userRepository.save(client);


    }



    
}
