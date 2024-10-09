package io.bootify.ecommerce_app.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String description;

    private Double price;

    private Integer stock;

    private String imageUrl;

    private String addedby;

    private Long categoryId;
    private String categoryName;

    private Long brandId;
    private String brandName;

    private Integer totalReviews;
    private Double averageRating;

}
