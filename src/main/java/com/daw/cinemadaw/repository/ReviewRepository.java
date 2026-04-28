package com.daw.cinemadaw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.user.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Totes les reviews d'una pel·lícula, ordenades de més nova a més antiga
       List<Review> findByMovieIdOrderByCreatedAtDesc(Long movieId);

    // Comprova si un usuari ja ha valorat una pel·lícula
       boolean existsByMovieIdAndUserId(Long movieId, Long userId);

    // Obté la review concreta d'un usuari per una pel·lícula (per editar/eliminar)
       Optional<Review> findByMovieIdAndUserId(Long movieId, Long userId);

// Mitjana de puntuació d'una pel·lícula
       @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId")
       Double findAverageRatingByMovieId(@Param("movieId") Long movieId);

// Comprova si l'usuari té alguna entrada comprada per a la pel·lícula
// (entrada.order != null vol dir que la compra es va confirmar)
@Query("SELECT COUNT(e) > 0 FROM Entrada e " + "WHERE e.user.id = :userId " + "AND e.screening.movie.id = :movieId " + "AND e.order IS NOT NULL")
       boolean userHasBoughtTicketForMovie(@Param("userId") Long userId,@Param("movieId") Long movieId);
}
