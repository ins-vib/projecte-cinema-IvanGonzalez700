package com.daw.cinemadaw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.cinema.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    //CRUD

    //create save()

    //read getbyID()

    //update
    
    //delete delete()
}
