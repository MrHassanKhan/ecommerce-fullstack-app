package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.domain.Cart;
import io.bootify.ecommerce_app.domain.Order;
import io.bootify.ecommerce_app.domain.Product;
import io.bootify.ecommerce_app.model.ProductDTO;
import io.bootify.ecommerce_app.model.requestDTO.ShopFilterDto;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import io.bootify.ecommerce_app.repos.CartRepository;
import io.bootify.ecommerce_app.repos.OrderRepository;
import io.bootify.ecommerce_app.repos.ProductRepository;
import io.bootify.ecommerce_app.specification.ProductSpecification;
import io.bootify.ecommerce_app.util.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.bootify.ecommerce_app.util.SecurityAppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final AppUserService appUserService;
    private final FileService fileService;
    private final ReviewService reviewService;

    @Value("${project.imageFolder}")
    private String imageFolder;
    @Value("${project.url}")
    private String baseUrl;

    public ProductService(final ProductRepository productRepository, CategoryService categoryService, BrandService brandService,
                          final AppUserService appUserService, FileService fileService, ReviewService reviewService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.appUserService = appUserService;
        this.fileService = fileService;
        this.reviewService = reviewService;
    }

//    @Transactional
    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public List<ProductDTO> findMyProducts() {
        final List<Product> products = productRepository.findAllByAddedby_Id(appUserService.getAuthenticatedUser().getId());
        return products.stream()
            .map(this::mapToDTO)
            .toList();
    }


    public ProductDTO get(final Long id) {
        return productRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    /**
     * Creates a product with given data.
     *
     * <p>If given {@link MultipartFile} is not null, it will be uploaded to the server and the product's image URL will be updated.
     *
     * @param productDTO the new product data
     * @param file       the new image of the product
     *
     * @return the created product's id
     *
     * @throws IOException if the image could not be uploaded
     */
    public Long create(final ProductDTO productDTO, MultipartFile file) throws IOException {
        if(Files.exists(Paths.get(imageFolder + file.getOriginalFilename()))) {
            throw new RuntimeException("File already exists! Please choose a different one filename.");
        }
        String uploadedFileName = fileService.uploadFile(imageFolder, file);
        productDTO.setImageUrl(uploadedFileName);
        final Product product = mapToEntity(productDTO);
        return productRepository.save(product).getId();
    }

    /**
     * Updates a product with given id.
     *
     * <p>If given {@link MultipartFile} is not null, it will be uploaded to the server and the product's image URL will be updated.
     *
     * @param id             the id of the product to update
     * @param productDTO     the new product data
     * @param file           the new image of the product
     *
     * @return the updated product's id
     *
     * @throws IOException if the image could not be uploaded
     */
    public Long update(final Long id, final ProductDTO productDTO, MultipartFile file) throws IOException {
        if(file != null) {
            if(Files.exists(Paths.get(imageFolder + file.getOriginalFilename()))) {
                throw new RuntimeException("File already exists! Please choose a different one filename.");
            }
            String uploadedFileName = fileService.uploadFile(imageFolder, file);
            productDTO.setImageUrl(uploadedFileName);
        }
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        if(productDTO.getImageUrl() != null) {
            BeanUtils.copyProperties(productDTO, product, "id", "addedby");
        } else {
            BeanUtils.copyProperties(productDTO, product, "id", "addedby", "imageUrl");
        }

        return productRepository.save(product).getId();
    }

    public void delete(final Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Page<ProductDTO> getShopProducts(ShopFilterDto filter, int page, int size, String sortField, String sortDirection) {
        Pageable pageable = PageRequest.of(page, size, sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
//        return productRepository.findProductsByFilter(
//            filter.getCategorieIds() ,
//            filter.getBrandIds(),
//            filter.getMinPrice(),
//            filter.getMaxPrice(),
//            pageable
//        ).map(this::mapToDTO);
        return productRepository.findProductsByFilter(
            (filter.getCategorieIds() != null && !filter.getCategorieIds().isEmpty()) ? filter.getCategorieIds() : null,
            (filter.getBrandIds() != null && !filter.getBrandIds().isEmpty()) ? filter.getBrandIds() : null,
            (filter.getMinPrice() != null && filter.getMinPrice() > 0) ? filter.getMinPrice() : null,
            (filter.getMaxPrice() != null && filter.getMaxPrice() > 0) ? filter.getMaxPrice() : null,
             pageable).map(this::mapToDTO);
    }


    public Page<ProductDTO> getProductsForShop(String searchText,
                                               String searchField,
                                               Double minPrice, Double maxPrice,
                                               int page, int size, String sortField, String sortDirection) {


//        Example<Product> example = Example.of(new Product(), ExampleMatcher.matching()
//            .withMatcher(searchField, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()));
//
//        Pageable pageable = PageRequest.of(page, size, sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
//        return productRepository.findAll(pageable).map(this::mapToDTO);

//        Specification<Product> spec = Specification.where(null);
//        if (searchText != null && !searchText.isEmpty()) {
//            spec = spec.and((root, query, cb) -> cb.like(root.get(searchField), "%" + searchText + "%"));
//        }
//        if (minPrice != null) {
//            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
//        }
//        if (maxPrice != null) {
//            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
//        }

        // Build specifications for filtering
        Specification<Product> specification = Specification
            .where(ProductSpecification.containsNameOrDescription(searchText, searchField))
            .and(ProductSpecification.hasPriceBetween(minPrice, maxPrice));
        Pageable pageable = PageRequest.of(page, size, sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        return productRepository.findAll(specification, pageable).map(this::mapToDTO);
    }



    private ProductDTO mapToDTO(final Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setAddedby(product.getAddedby() == null ? null : product.getAddedby().getFullname());
        productDTO.setImageUrl(product.getImageUrl() == null ? null : baseUrl + "/file/" + product.getImageUrl());
        productDTO.setCategoryId(product.getCategory() == null ? null : product.getCategory().getId());
        productDTO.setBrandId(product.getBrand() == null ? null : product.getBrand().getId());
        productDTO.setCategoryName(product.getCategory() == null ? null : product.getCategory().getName());
        productDTO.setBrandName(product.getBrand() == null ? null : product.getBrand().getName());
        productDTO.setTotalReviews(reviewService.countReviewsByProductId(product.getId()));
        productDTO.setAverageRating(reviewService.getAverageRatingByProductId(product.getId()));
        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());
        product.setAddedby(appUserService.getAuthenticatedUser());
        product.setCategory(productDTO.getCategoryId() == null ? null : categoryService.findById(productDTO.getCategoryId()));
        product.setBrand(productDTO.getBrandId() == null ? null : brandService.findById(productDTO.getBrandId()));
        return product;
    }

    public Page<ProductDTO> searchProducts(String searchField, String searchValue, int page, int size, String sortField, String sortDirection) {

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher(searchField, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Product> example = Example.of(new Product(), matcher);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
            Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(example, pageable).map(this::mapToDTO);
    }
}
