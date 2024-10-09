package io.bootify.ecommerce_app.rest;

import io.bootify.ecommerce_app.domain.Category;
import io.bootify.ecommerce_app.model.CategoryProductCountDTO;
import io.bootify.ecommerce_app.model.Role;
import io.bootify.ecommerce_app.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + Role.Fields.CUSTOMER + "', '" + Role.Fields.ADMIN + "')")
@SecurityRequirement(name = "bearer-jwt")
public class CategoryResource {

    final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategoryValues() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }


    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.save(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "id") final Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
