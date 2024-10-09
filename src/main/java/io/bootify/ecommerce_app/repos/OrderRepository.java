package io.bootify.ecommerce_app.repos;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByAppUserId(Long userId);
    Order findByStripePaymentIntentId(String paymentIntentId);

}
