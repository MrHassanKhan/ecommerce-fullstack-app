package io.bootify.ecommerce_app.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CartDTO {

    private Long id;
    private Double totalAmount;
    private Long userId;
    private List<CartItemDTO> cartItems;
}

