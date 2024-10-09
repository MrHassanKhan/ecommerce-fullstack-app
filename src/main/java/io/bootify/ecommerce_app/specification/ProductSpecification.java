package io.bootify.ecommerce_app.specification;

import io.bootify.ecommerce_app.domain.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    // Specification to filter by price between minPrice and maxPrice
    public static Specification<Product> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return criteriaBuilder.conjunction();  // Always true, no price filter
        };
    }

    // Specification to filter by name or description containing the search text (case-insensitive)
    public static Specification<Product> containsNameOrDescription(String searchText, String searchField) {
        return (root, query, criteriaBuilder) -> {
            if (searchField ==null && searchText != null && !searchText.isEmpty()) {
                String likePattern = "%" + searchText.toLowerCase() + "%";
                return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
                );
            } else if (searchField != null && searchText != null && !searchText.isEmpty()) {
                String likePattern = "%" + searchText.toLowerCase() + "%";
                return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get(searchField)), likePattern)
                );
            }
            return criteriaBuilder.conjunction(); // Always true, no search text filter
        };
    }
}
