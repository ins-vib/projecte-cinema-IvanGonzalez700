package com.daw.cinemadaw.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        
    }
}
