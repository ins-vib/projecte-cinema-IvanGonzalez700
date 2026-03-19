package com.daw.cinemadaw.config;

import java.util.List;

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
    }
}
