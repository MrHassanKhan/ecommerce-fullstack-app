package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.Review;
import io.bootify.ecommerce_app.model.ReviewDTO;
import io.bootify.ecommerce_app.repos.ProductRepository;
import io.bootify.ecommerce_app.repos.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final AppUserService appUserService;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, AppUserService appUserService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.appUserService = appUserService;
    }

    public ReviewDTO save(ReviewDTO reviewDTO) {
        Review alreadySavedReview = reviewRepository.findByUserIdAndProductId(reviewDTO.getUserId(), reviewDTO.getProductId());
        if (alreadySavedReview != null) {
            reviewDTO.setId(alreadySavedReview.getId());
        }
        Review review = reviewRepository.save(mapToEntity(reviewDTO));
        return mapToDTO(review);
    }


    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    public Double getAverageRatingByProductId(Long productId) {
        return reviewRepository.getAverageRatingByProductId(productId);
    }

    public Integer countReviewsByProductId(Long productId) {
        return reviewRepository.countReviewsByProductId(productId);
    }

    public Page<ReviewDTO> getReviewsByProductId(Long id, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = PageRequest.of(page, size, sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return reviewRepository.getReviewsByProductId(id, pageable).map(this::mapToDTO);
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setProductId(review.getProduct().getId());
        reviewDTO.setUserId(review.getAppUser().getId());
        reviewDTO.setUserName(review.getAppUser().getUsername());
//        reviewDTO.setCreatedDate(review.getCreatedDate() == null ? null : review.getCreatedDate());
//        reviewDTO.setUpdatedDate(review.getUpdatedDate() == null ? null : review.getUpdatedDate());
        return reviewDTO;
    }

    private Review mapToEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setProduct(productRepository.findById(reviewDTO.getProductId()).get());
        review.setAppUser(appUserService.getAuthenticatedUser());
        return review;
    }


}
