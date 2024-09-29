package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.repository.BrandRepository;
import shop.biday.model.repository.CategoryRepository;
import shop.biday.model.repository.ProductRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.ImageService;
import shop.biday.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    @Override
    public List<Map.Entry<Long, ProductModel>> findByProductId(Long id) {
        log.info("Find Product by id: {}", id);

        if (!productRepository.existsById(id)) {
            log.error("Not found product: {}", id);
            return null;
        }

        Map<Long, ProductModel> map = productRepository.findByProductId(id,
                removeParentheses(productRepository.findById(id).get().getName()));

//        if (map != null) {
//            map.forEach((key, productModel) -> {
//                ImageModel imageModel = imageService.findByOriginalNameAndType(productModel.getProductCode(), "상품");
//                productModel.setImage(imageModel != null
//                        ? imageModel
//                        : imageService.findByTypeAndUploadPath("에러", "error"));
//                log.debug(imageModel != null
//                                ? "Product Image Found : {}"
//                                : "Product Image Not Found : {}",
//                        imageModel != null ? imageModel.getOriginalName() : imageService.findByTypeAndUploadPath("에러", "error").getOriginalName());
//            });
//        }

        return Objects.requireNonNull(map).entrySet().stream().toList();
    }

    public static String removeParentheses(String productName) {
        int index = productName.indexOf(" (");
        if (index != -1) {
            return productName.substring(0, index);
        }
        return productName;
    }

    @Override
    public ProductEntity save(String token, ProductModel product) {
        log.info("Save Product started");
        return validateUser(token)
                .map(t -> productRepository.save(ProductEntity.builder()
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
                    boolean exists = productRepository.existsById(product.getId());
                    if (!exists) {
                        log.error("Not found product: {}", product.getId());
                    }
                    return exists;
                })
                .map(t -> productRepository.save(ProductEntity.builder()
                        .id(product.getId())
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
    public String deleteById(String token, Long id) {
        log.info("Delete Product started");

        return validateUser(token).map(t -> {
            if (!productRepository.existsById(id)) {
                log.error("Not found product: {}", id);
                return "상품 삭제 실패";
            }

            productRepository.deleteById(id);
            log.info("Product deleted: {}", id);
            return "상품 삭제 성공";
        }).orElseGet(() -> {
            log.error("User does not have role ADMIN or does not exist");
            return "유효하지 않은 사용자";
        });
    }

    @Override
    public Slice<ProductDto> findByFilter(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId) {
        log.info("Find Product by Filter started");
        log.info("Pageable: {} categoryId: {} brandId: {} keyword: {} color: {} order: {} lastItemId: {}", pageable, categoryId, brandId, keyword, color, order, lastItemId);
        return productRepository.findProducts(pageable, categoryId, brandId, keyword, color, order, lastItemId);
    }

    private Optional<String> validateUser(String token) {
        log.info("Validate User started");
        return Optional.of(token)
                .filter(t -> jwtUtil.getRole(t).equalsIgnoreCase("ROLE_ADMIN"))
                .filter(t -> userRepository.existsByEmail(jwtUtil.getEmail(t)))
                .or(() -> {
                    log.error("User does not have role ADMIN or does not exist");
                    return Optional.empty();
                });
    }

}
