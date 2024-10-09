package io.bootify.ecommerce_app.model;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;
}
