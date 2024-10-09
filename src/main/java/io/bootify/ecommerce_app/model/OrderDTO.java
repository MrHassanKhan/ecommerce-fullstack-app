package io.bootify.ecommerce_app.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
public class OrderDTO {

    private Long id;

    @NotNull
    private Double totalAmount;

    @NotNull
    private OrderStatus status;

    @Size(max = 255)
    private String stripePaymentIntentId;

    private Long appUser;
    private List<OrderItemDTO> items;

    // Shipping information
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;

}

