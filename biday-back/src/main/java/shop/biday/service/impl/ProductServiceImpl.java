package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.repository.BrandRepository;
import shop.biday.model.repository.CategoryRepository;
import shop.biday.model.repository.ProductRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.ProductService;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductEntity save(String token, ProductModel product) {
        log.info("Save Product started");
        return validateUser(token)
                .map(t -> repository.save(ProductEntity.builder()
                        .brand(brandRepository.findByName(product.getBrand()))
                        .category(categoryRepository.findByName(product.getCategory()))
                        .name(product.getName())
                        .subName(product.getSubName())
                        .productCode(product.getProductCode())
                        .price(product.getPrice())
                        .color(product.getColor())
                        .description(product.getDescription())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Save Product failed"));
    }

    @Override
    public ProductEntity update(String token, ProductModel product) {
        log.info("Update Product started");
        return validateUser(token)
                .filter(t -> {
                    boolean exists = repository.existsById(product.getId());
                    if (!exists) {
                        log.error("Not found product: {}", product.getId());
                    }
                    return exists;
                })
                .map(t -> repository.save(ProductEntity.builder()
                        .brand(brandRepository.findByName(product.getBrand()))
                        .category(categoryRepository.findByName(product.getCategory()))
                        .name(product.getName())
                        .subName(product.getSubName())
                        .productCode(product.getProductCode())
                        .price(product.getPrice())
                        .color(product.getColor())
                        .description(product.getDescription())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Update Product failed: Product not found"));
    }

    @Override
    public void deleteById(String token, Long id) {
        log.info("Delete Product started");
        validateUser(token)
                .filter(t -> {
                    boolean exists = repository.existsById(id);
                    if (!exists) {
                        log.error("Not found product: {}", id);
                    }
                    return exists;
                })
                .ifPresentOrElse(t -> {
                    repository.deleteById(id);
                    log.info("Product deleted: {}", id);
                }, () -> log.error("User does not have role SELLER or does not exist"));
    }

    @Override
    public ProductModel findById(Long id) {
        log.info("Find Product by id: {}", id);
        return Optional.of(id)
                .filter(t -> {
                    boolean exists = repository.existsById(t);
                    if (!exists) {
                        log.error("Not found product: {}", id);
                    }
                    return exists;
                })
                .map(t -> repository.findByProductId(id))
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Slice<ProductDto> findByFilter(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId) {
        log.info("Find Product by Filter started");
        log.info("Pageable: {} categoryId: {} brandId: {} keyword: {} color: {} order: {} lastItemId: {}", pageable, categoryId, brandId, keyword, color, order, lastItemId);
        return repository.findProducts(pageable, categoryId, brandId, keyword, color, order, lastItemId);
    }

    private Optional<String> validateUser(String token) {
        log.info("Validate User started");
        return Optional.of(token)
                .filter(t -> jwtUtil.getRole(t).equalsIgnoreCase("ROLE_SELLER"))
                .filter(t -> userRepository.existsByEmail(jwtUtil.getEmail(t)))
                .or(() -> {
                    log.error("User does not have role SELLER or does not exist");
                    return Optional.empty();
                });
    }

}
