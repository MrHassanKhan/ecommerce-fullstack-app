package io.bootify.ecommerce_app.repos;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.domain.Order;
import io.bootify.ecommerce_app.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {


}
