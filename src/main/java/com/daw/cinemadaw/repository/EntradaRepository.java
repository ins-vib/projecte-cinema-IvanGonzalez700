package com.daw.cinemadaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.cinema.Entrada;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {

    boolean existsByScreeningAndSeat(Screening screening, Seat seat);
    List<Entrada> findByScreening(Screening screening);
    
}
