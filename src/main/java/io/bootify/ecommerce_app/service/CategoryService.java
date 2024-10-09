package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.Category;
import io.bootify.ecommerce_app.model.CategoryProductCountDTO;
import io.bootify.ecommerce_app.repos.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
    public List<CategoryProductCountDTO> getCategoriesWithProductCount() {
        return categoryRepository.findCategoriesWithProductCount();
    }
}
