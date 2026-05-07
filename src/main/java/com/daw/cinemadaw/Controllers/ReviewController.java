package com.daw.cinemadaw.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.cinemadaw.domain.cinema.Movie;
import com.daw.cinemadaw.domain.user.Review;
import com.daw.cinemadaw.domain.user.User;
import com.daw.cinemadaw.repository.MovieRepository;
import com.daw.cinemadaw.repository.ReviewRepository;
import com.daw.cinemadaw.repository.UserRepository;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/movies/{id}/reviews")
    public String showReviews(@PathVariable Long id, Model model) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        if (movieOpt.isEmpty()) {
            return "redirect:/movies/user";
        }

        List<Review> reviews = reviewRepository.findByMovieIdOrderByCreatedAtDesc(id);
        Double average = reviewRepository.findAverageRatingByMovieId(id);
        // Arrodonir a 1 decimal si hi ha mitjana
        if (average != null) {
            average = Math.round(average * 10.0) / 10.0;
        }

        model.addAttribute("movie", movieOpt.get());
        model.addAttribute("reviews", reviews);
        model.addAttribute("average", average);
        model.addAttribute("totalReviews", reviews.size());
        model.addAttribute("newReview", new Review());
        model.addAttribute("currentUser", getUsuariActual());
        return "reviews/reviews";
    }

    @PostMapping("/movies/{id}/reviews")
    public String createReview(@PathVariable Long id,
                               @ModelAttribute Review review,
                               RedirectAttributes redirectAttrs) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        if (movieOpt.isEmpty()) {
            return "redirect:/movies/user";
        }

        User user = getUsuariActual();
        if (user == null) {
            return "redirect:/login";
        }

        // Validació mínima: rating ha d'estar entre 1 i 5
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            redirectAttrs.addFlashAttribute("error", "Has de seleccionar una puntuació entre 1 i 5.");
            return "redirect:/movies/" + id + "/reviews";
        }

        review.setId(null);
        review.setCreatedAt(java.time.LocalDateTime.now());
        review.setMovie(movieOpt.get());
        review.setUser(user);
        reviewRepository.save(review);

        redirectAttrs.addFlashAttribute("success", "Valoració publicada correctament!");
        return "redirect:/movies/" + id + "/reviews";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        Optional<Review> reviewOpt = reviewRepository.findById(id);
        if (reviewOpt.isEmpty()) {
            return "redirect:/movies/user";
        }
        Long movieId = reviewOpt.get().getMovie().getId();

        // Només pot eliminar el propi autor
        User user = getUsuariActual();
        if (user != null && reviewOpt.get().getUser().getId().equals(user.getId())) {
            reviewRepository.delete(reviewOpt.get());
        }

        return "redirect:/movies/" + movieId + "/reviews";
    }

    private User getUsuariActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        return userRepository.findByUsername(auth.getName()).orElse(null);
    }
}