package io.bootify.ecommerce_app.repos;

import io.bootify.ecommerce_app.domain.Category;
import io.bootify.ecommerce_app.model.CategoryProductCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new io.bootify.ecommerce_app.model.CategoryProductCountDTO(c.id, c.name, count(p)) " +
            "FROM Category c LEFT JOIN Product p on p.category.id = c.id group by c.id, c.name")
    List<CategoryProductCountDTO> findCategoriesWithProductCount();
}
