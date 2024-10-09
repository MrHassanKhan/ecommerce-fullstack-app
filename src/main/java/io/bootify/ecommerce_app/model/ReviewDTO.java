package io.bootify.ecommerce_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private Long productId;
    private Long userId;
    private String userName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
