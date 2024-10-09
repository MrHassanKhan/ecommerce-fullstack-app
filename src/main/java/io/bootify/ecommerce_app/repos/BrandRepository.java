package io.bootify.ecommerce_app.repos;

import io.bootify.ecommerce_app.domain.Brand;
import io.bootify.ecommerce_app.model.BrandProductCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("select new io.bootify.ecommerce_app.model.BrandProductCountDTO(b.id, b.name, count(p)) " +
        "from Brand b left join Product p on b.id = p.brand.id group by b.id, b.name")
    List<BrandProductCountDTO> findBrandsWithProductCount();
}
