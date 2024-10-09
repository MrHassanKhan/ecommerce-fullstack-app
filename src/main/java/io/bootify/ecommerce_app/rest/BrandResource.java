package io.bootify.ecommerce_app.rest;

import io.bootify.ecommerce_app.domain.Brand;
import io.bootify.ecommerce_app.model.BrandProductCountDTO;
import io.bootify.ecommerce_app.model.CategoryProductCountDTO;
import io.bootify.ecommerce_app.model.Role;
import io.bootify.ecommerce_app.service.BrandService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/brands", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + Role.Fields.CUSTOMER + "', '" + Role.Fields.ADMIN + "')")
@SecurityRequirement(name = "bearer-jwt")
public class BrandResource {
    final BrandService brandService;

    public BrandResource(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<List<Brand>> getBrandValues() {
        return ResponseEntity.ok(brandService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrand(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(brandService.findById(id));
    }


    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.save(brand));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable(name = "id") final Long id) {
        brandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
