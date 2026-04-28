package com.daw.cinemadaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.cinema.Entrada;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.user.User;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {

    boolean existsByScreeningAndSeat(Screening screening, Seat seat);
    List<Entrada> findByScreening(Screening screening);
    List<Entrada> findByUser(User user);
    List<Entrada> findByUserAndOrderIsNull(User user);

    // Only checks purchased entries (that have an associated Order)
    boolean existsByScreeningAndSeatAndOrderIsNotNull(Screening screening, Seat seat);
    List<Entrada> findByScreeningAndOrderIsNotNull(Screening screening);

    // Finds all entries in cart (not yet purchased) for a screening
    boolean existsByScreeningAndSeatAndOrderIsNull(Screening screening, Seat seat);
    List<Entrada> findByScreeningAndOrderIsNull(Screening screening);

    // Checks if the user already has this seat in the cart (not yet purchased)
    boolean existsByScreeningAndSeatAndUserAndOrderIsNull(Screening screening, Seat seat, User user);

    // Checks if another user (not the given one) already has this seat in their cart
    boolean existsByScreeningAndSeatAndUserNotAndOrderIsNull(Screening screening, Seat seat, User user);
    List<Entrada> findByScreeningAndSeatAndUserNotAndOrderIsNull(Screening screening, Seat seat, User user);

    // Find all entradas for a specific seat (needed for cascade deletion)
    List<Entrada> findBySeat(Seat seat);

}
