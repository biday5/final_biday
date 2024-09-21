package shop.biday.service;

import shop.biday.model.domain.CategoryModel;
import shop.biday.model.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryEntity> findAll();
    Optional<CategoryEntity> findById(Long id);
    CategoryEntity save(String token, CategoryModel category);
    CategoryEntity update(String token, CategoryModel category);
    void deleteById(String token, Long id);
}
