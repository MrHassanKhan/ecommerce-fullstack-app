package io.bootify.ecommerce_app.rest;

import io.bootify.ecommerce_app.model.BrandProductCountDTO;
import io.bootify.ecommerce_app.model.CategoryProductCountDTO;
import io.bootify.ecommerce_app.model.ProductDTO;
import io.bootify.ecommerce_app.model.ReviewDTO;
import io.bootify.ecommerce_app.model.requestDTO.ShopFilterDto;
import io.bootify.ecommerce_app.service.BrandService;
import io.bootify.ecommerce_app.service.CategoryService;
import io.bootify.ecommerce_app.service.ProductService;
import io.bootify.ecommerce_app.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/public", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicResource {
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    public PublicResource(ProductService productService, BrandService brandService,
                          CategoryService categoryService, ReviewService reviewService) {
        this.productService = productService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    @GetMapping("/searchProducts")
    public ResponseEntity<Page<ProductDTO>> searchProducts(@RequestParam(defaultValue = "name") String searchField,
                                                           @RequestParam(defaultValue = "") String searchValue,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String sortField,
                                                           @RequestParam(defaultValue = "desc") String sortDirection) {
        return ResponseEntity.ok(productService.searchProducts(searchField, searchValue, page, size, sortField, sortDirection));
    }

    @GetMapping("/filter")
    public Page<ProductDTO> getProducts(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) String searchField,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "price") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        return productService.getProductsForShop(searchText, searchField, minPrice, maxPrice, page, size, sortBy, sortDirection);
    }

    @PostMapping("/shop-filter")
    public Page<ProductDTO> getProductsByCategoriesAndBrands(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "price") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDirection,
        @RequestBody(required = false) ShopFilterDto filter
    ) {
        return productService.getShopProducts(filter, page, size, sortBy, sortDirection);
    }

    @GetMapping("/brand-product-count")
    public List<BrandProductCountDTO> getBrandsWithProductCount() {
        return brandService.getBrandsWithProductCount();
    }

    @GetMapping("/category-product-count")
    public List<CategoryProductCountDTO> getCategoriesWithProductCount() {
        return categoryService.getCategoriesWithProductCount();
    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(productService.get(id));
    }

}
