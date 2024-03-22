package com.sh.app.review.repository;

import com.sh.app.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
//    @Query("SELECT AVG(r.reviewScore) FROM Review r WHERE r.movie.id = :id")
//    Double getAverageRatingByMovieId(Long id);
}
