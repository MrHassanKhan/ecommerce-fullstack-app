package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.Brand;
import io.bootify.ecommerce_app.model.BrandProductCountDTO;
import io.bootify.ecommerce_app.model.CategoryProductCountDTO;
import io.bootify.ecommerce_app.repos.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public Brand findById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    public List<BrandProductCountDTO> getBrandsWithProductCount() {
        return brandRepository.findBrandsWithProductCount();
    }
}
