package io.bootify.ecommerce_app.repos;

import io.bootify.ecommerce_app.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // get average rating of a product
    @Query("select avg(r.rating) from Review r where r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Long productId);

    @Query("select r from Review r where r.appUser.id = :userId and r.product.id = :productId")
    Review findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Query("select r from Review r where r.product.id = :productId")
    Page<Review> getReviewsByProductId(@Param("productId") Long productId, Pageable pageable);

    @Query("select count(r) from Review r where r.product.id = :productId")
    Integer countReviewsByProductId(@Param("productId") Long productId);
}
