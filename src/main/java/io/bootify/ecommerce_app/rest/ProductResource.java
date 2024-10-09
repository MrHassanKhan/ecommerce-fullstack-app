package io.bootify.ecommerce_app.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bootify.ecommerce_app.domain.*;
import io.bootify.ecommerce_app.model.ProductDTO;
import io.bootify.ecommerce_app.model.Role;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import io.bootify.ecommerce_app.repos.CartRepository;
import io.bootify.ecommerce_app.repos.OrderRepository;
import io.bootify.ecommerce_app.service.BrandService;
import io.bootify.ecommerce_app.service.CategoryService;
import io.bootify.ecommerce_app.service.ProductService;
import io.bootify.ecommerce_app.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + Role.Fields.CUSTOMER + "', '" + Role.Fields.VENDOR + "', '" + Role.Fields.ADMIN + "')")
@SecurityRequirement(name = "bearer-jwt")
public class ProductResource {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductResource(final ProductService productService, CategoryService categoryService, BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }


    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/myProducts")
    public ResponseEntity<List<ProductDTO>> getMyProducts() {
        return ResponseEntity.ok(productService.findMyProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createProduct(@RequestPart MultipartFile file, @RequestPart String productDTO) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO dto = objectMapper.readValue(productDTO, ProductDTO.class);
        if(file.isEmpty()) {
            file = null;
        }
        final Long createdId = productService.create(dto, file);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProduct(@PathVariable(name = "id") final Long id,
                                              @RequestPart(required = false) MultipartFile file, @RequestPart String productDTO) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO dto = objectMapper.readValue(productDTO, ProductDTO.class);
        if(file.isEmpty()) {
            file = null;
        }
        final Long createdId = productService.update(id, dto, file);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") final Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoryValues")
    public ResponseEntity<Map<Long, String>> getCategoryValues() {
        return ResponseEntity.ok(categoryService.findAll()
                .stream()
                .collect(CustomCollectors.toSortedMap(Category::getId, Category::getName)));
    }

    @GetMapping("/brandValues")
    public ResponseEntity<Map<Long, String>> getBrandValues() {
        return ResponseEntity.ok(brandService.findAll()
                .stream()
                .collect(CustomCollectors.toSortedMap(Brand::getId, Brand::getName)));
    }

//    @GetMapping("/addedbyValues")
//    public ResponseEntity<Map<Long, String>> getAddedbyValues() {
//        return ResponseEntity.ok(appUserRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(AppUser::getId, AppUser::getUsername)));
//    }

//    @GetMapping("/orderValues")
//    public ResponseEntity<Map<Long, Long>> getOrderValues() {
//        return ResponseEntity.ok(orderRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Order::getId, Order::getId)));
//    }
//
//    @GetMapping("/cartValues")
//    public ResponseEntity<Map<Long, Long>> getCartValues() {
//        return ResponseEntity.ok(cartRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Cart::getId, Cart::getId)));
//    }

}
