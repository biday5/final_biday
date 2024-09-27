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

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    @Override
    public List<Map.Entry<Long, ProductModel>> findByProductId(Long id) {
        log.info("Find Product by id: {}", id);

        if (!repository.existsById(id)) {
            log.error("Not found product: {}", id);
            return null;
        }

        Map<Long, ProductModel> map = repository.findByProductId(id,
                removeParentheses(repository.findById(id).get().getName()));

        if (map != null) {
            map.forEach((key, productModel) -> {
                ImageModel imageModel = imageService.findByOriginalNameAndType(productModel.getProductCode(), "상품");
                productModel.setImage(imageModel != null
                        ? imageModel
                        : imageService.findByTypeAndUploadPath("에러", "error"));
                log.debug(imageModel != null
                                ? "Product Image Found : {}"
                                : "Product Image Not Found : {}",
                        imageModel != null ? imageModel.getOriginalName() : imageService.findByTypeAndUploadPath("에러", "error").getOriginalName());
            });
        }

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
                .map(t -> repository.save(ProductEntity.builder()
                        .brand(brandRepository.findByName(product.getBrand()))
                        .category(categoryRepository.findByName(product.getCategory()))
                        .name(product.getName())
                        .subName(product.getSubName())
                        .productCode(product.getProductCode())
                        .price(product.getPrice())
                        .color(product.getColor())
                        .description(product.getDescription())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
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
                        .id(product.getId())
                        .brand(brandRepository.findByName(product.getBrand()))
                        .category(categoryRepository.findByName(product.getCategory()))
                        .name(product.getName())
                        .subName(product.getSubName())
                        .productCode(product.getProductCode())
                        .price(product.getPrice())
                        .color(product.getColor())
                        .description(product.getDescription())
                        .createdAt(product.getCreatedAt())
                        .updatedAt(LocalDateTime.now())
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

    // 강사님이 말씀하신대로 한 것
    /*public Map<Long, GroupedProduct> findByProductTest(Long id) {
        List<ProductTest> products = repository.findByProductTest(id);

        if (!products.isEmpty()) {
            Map<Long, GroupedProduct> groupedProducts = products.stream()
                    .collect(Collectors.groupingBy(
                            ProductTest::getId,
                            Collectors.mapping(product -> {
                                String size = product.getSizes().getSize();
                                Long auctionId = product.getSizes().getAuctions().getId();
                                return new AbstractMap.SimpleEntry<>(size, auctionId);
                            }, Collectors.toList())
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        Long productId = entry.getKey();
                        List<AbstractMap.SimpleEntry<String, Long>> sizeAuctionPairs = entry.getValue();

                        Map<String, List<Long>> sizeToAuctionIds = sizeAuctionPairs.stream()
                                .collect(Collectors.groupingBy(
                                        AbstractMap.SimpleEntry::getKey,
                                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())
                                ));

                        List<GroupedSize> sizes = sizeToAuctionIds.entrySet().stream()
                                .map(sizeEntry -> {
                                    String size = sizeEntry.getKey();
                                    List<GroupedAuction> auctions = sizeEntry.getValue().stream()
                                            .map(GroupedAuction::new)
                                            .collect(Collectors.toList());
                                    return new GroupedSize(size, auctions);
                                })
                                .collect(Collectors.toList());

                        ProductTest sampleProduct = products.stream()
                                .filter(p -> p.getId().equals(productId))
                                .findFirst()
                                .orElse(null);

                        return new GroupedProduct(productId, sampleProduct.getName(), sizes);
                    })
                    .collect(Collectors.toMap(GroupedProduct::getId, groupedProduct -> groupedProduct));

            // 결과 확인
            groupedProducts.forEach((key, value) -> {
                System.out.println("Product ID: " + value.getId() + ", Name: " + value.getName());
                value.getSizesWithAuctions().forEach(size -> {
                    System.out.println("  Size: " + size.getSize() + ", Auction IDs: " + size.getAuctionIds().stream()
                            .map(GroupedAuction::getId)
                            .collect(Collectors.toList()));
                });
            });
            return groupedProducts;
        } else return null;
    }*/

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
