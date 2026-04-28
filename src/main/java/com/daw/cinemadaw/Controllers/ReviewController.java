package com.daw.cinemadaw.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("movie", movieRepository.findById(id).get());
        model.addAttribute("reviews", reviewRepository.findByMovieIdOrderByCreatedAtDesc(id));
        model.addAttribute("average", reviewRepository.findAverageRatingByMovieId(id));
        model.addAttribute("newReview", new Review());
        model.addAttribute("currentUser", getUsuariActual());
        return "reviews/reviews";
    }

    @PostMapping("/movies/{id}/reviews")
    public String createReview(@PathVariable Long id, @ModelAttribute Review review) {
        review.setId(null);
        review.setCreatedAt(java.time.LocalDateTime.now());
        review.setMovie(movieRepository.findById(id).get());
        review.setUser(getUsuariActual());
        reviewRepository.save(review);
        return "redirect:/movies/" + id + "/reviews";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        Review review = reviewRepository.findById(id).get();
        Long movieId = review.getMovie().getId();
        reviewRepository.delete(review);
        return "redirect:/movies/" + movieId + "/reviews";
    }

    private User getUsuariActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElse(null);
    }
}