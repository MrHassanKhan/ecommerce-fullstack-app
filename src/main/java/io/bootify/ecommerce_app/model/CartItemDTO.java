package io.bootify.ecommerce_app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {

    private Long id;
    private Double price;
    private Long productId;
    private String productName;
    private String imageUrl;
    private int quantity;
}
