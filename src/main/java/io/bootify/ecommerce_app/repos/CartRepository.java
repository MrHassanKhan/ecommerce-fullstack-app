package io.bootify.ecommerce_app.repos;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByAppUserId(Long userId);

}
