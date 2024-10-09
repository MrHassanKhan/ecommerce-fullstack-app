package io.bootify.ecommerce_app.repos;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.domain.Cart;
import io.bootify.ecommerce_app.domain.Order;
import io.bootify.ecommerce_app.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByAddedby_Id(Long id);
    @Query("SELECT p FROM Product p " +
        "WHERE (:categoryIds IS NULL OR p.category.id IN :categoryIds) " +
        "AND (:brandIds IS NULL OR p.brand.id IN :brandIds) " +
        "AND ((:minPrice IS NULL OR :minPrice = 0) OR p.price >= :minPrice) " +
        "AND ((:maxPrice IS NULL OR :maxPrice = 0) OR p.price <= :maxPrice)")
    Page<Product> findProductsByFilter(@Param("categoryIds") List<Long> categoryIds, @Param("brandIds") List<Long> brandIds,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        Pageable pageable);
}
