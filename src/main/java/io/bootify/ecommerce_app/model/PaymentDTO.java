package io.bootify.ecommerce_app.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentDTO {

    private Long id;
    private Double amount;
    private PaymentStatus status;
    private String userFullName;
    private Long orderId;
    private String currency;

}
