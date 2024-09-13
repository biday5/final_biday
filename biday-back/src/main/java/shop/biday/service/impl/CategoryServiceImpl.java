package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.CategoryModel;
import shop.biday.model.entity.CategoryEntity;
import shop.biday.model.repository.CategoryRepository;
import shop.biday.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> findAll() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<CategoryEntity> findById(Long id) {
        try {
            return categoryRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public CategoryEntity save(CategoryModel category) {
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public CategoryEntity update(CategoryModel category) {
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
