package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.CategoryModel;
import shop.biday.model.entity.CategoryEntity;
import shop.biday.model.repository.CategoryRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.CategoryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public List<CategoryEntity> findAll() {
        log.info("Find all categories");
        return categoryRepository.findAll();
    }

    @Override
    public Optional<CategoryEntity> findById(Long id) {
        log.info("Find category by id: {}", id);
        return Optional.of(id)
                .filter(t -> {
                    boolean exists = categoryRepository.existsById(t);
                    if (!exists) {
                        log.error("Not found category: {}", id);
                    }
                    return exists;
                })
                .flatMap(categoryRepository::findById);
    }

    @Override
    public CategoryEntity save(String token, CategoryModel category) {
        log.info("Save Category started");
        return validateUser(token)
                .map(t -> categoryRepository.save(CategoryEntity.builder()
                        .name(category.getName())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Save Category failed"));
    }

    @Override
    public CategoryEntity update(String token, CategoryModel category) {
        log.info("Update Category started");
        return validateUser(token)
                .filter(t -> {
                    boolean exists = categoryRepository.existsById(category.getId());
                    if (!exists) {
                        log.error("Not found category: {}", category.getId());
                    }
                    return exists;
                })
                .map(t -> categoryRepository.save(CategoryEntity.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Update Category failed: Category not found"));
    }

    @Override
    public String deleteById(String token, Long id) {
        log.info("Delete Category started for id: {}", id);

        return validateUser(token).map(t -> {
            if (!categoryRepository.existsById(id)) {
                log.error("Not found category: {}", id);
                return "카테고리 삭제 실패";
            }

            categoryRepository.deleteById(id);
            log.info("Category deleted: {}", id);
            return "카테고리 삭제 성공";
        }).orElseGet(() -> {
            log.error("User does not have role ADMIN or does not exist");
            return "유효하지 않은 사용자";
        });
    }

    private Optional<String> validateUser(String token) {
        log.info("Validate User started for token: {}", token);
        return Optional.of(token)
                .filter(t -> jwtUtil.getRole(t).equalsIgnoreCase("ROLE_ADMIN"))
                .filter(t -> userRepository.existsByEmail(jwtUtil.getEmail(t)))
                .or(() -> {
                    log.error("User does not have role ADMIN or does not exist for token: {}", token);
                    return Optional.empty();
                });
    }
}
