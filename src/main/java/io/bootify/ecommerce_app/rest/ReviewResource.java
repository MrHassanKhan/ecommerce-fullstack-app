package io.bootify.ecommerce_app.rest;

import io.bootify.ecommerce_app.model.ReviewDTO;
import io.bootify.ecommerce_app.model.Role;
import io.bootify.ecommerce_app.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewResource {
    private final ReviewService reviewService;
    public ReviewResource(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasAnyAuthority('" + Role.Fields.CUSTOMER + "', '" + Role.Fields.VENDOR + "', '" + Role.Fields.ADMIN + "')")
    @SecurityRequirement(name = "bearer-jwt")
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.save(reviewDTO));
    }
    @PreAuthorize("hasAnyAuthority('" + Role.Fields.CUSTOMER + "', '" + Role.Fields.VENDOR + "', '" + Role.Fields.ADMIN + "')")
    @SecurityRequirement(name = "bearer-jwt")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getAverageRatingByProductId/{productId}")
    public ResponseEntity<Double> getAverageRatingByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getAverageRatingByProductId(productId));
    }

    @GetMapping("/getReviewsByProductId/{productId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByProductId(
         @PathVariable Long productId,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "id") String sortBy,
         @RequestParam(defaultValue = "DESC") String sortDirection) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId, page, size, sortBy, sortDirection));
    }
}
