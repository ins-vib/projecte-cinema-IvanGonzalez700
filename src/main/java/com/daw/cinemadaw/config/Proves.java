package com.daw.cinemadaw.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.repository.CinemaRepository;

@Component
public class Proves implements CommandLineRunner {

    private CinemaRepository cinemaRepository;

    
    public Proves(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        

        Cinema cinema1 = new Cinema("Ocine", "Gavarres, 46", "Tarragona", "43122");

        cinemaRepository.save(cinema1);
    }
    
}
