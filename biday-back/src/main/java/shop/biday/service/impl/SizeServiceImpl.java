package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.entity.SizeEntity;
import shop.biday.model.entity.enums.Size;
import shop.biday.model.repository.ProductRepository;
import shop.biday.model.repository.SizeRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.SizeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository repository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Override
    public List<SizeEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<SizeEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<SizeEntity> findAllByProductId(Long productId) {
        return repository.findAllByProductId(productId);
    }

    @Override
    public SizeEntity findBySize(String size) {
        return repository.findBySize(Size.valueOf(size));
    }

    @Override
    public SizeEntity save(String token, SizeModel size) {
        log.info("Save Size started");
        return validateUser(token)
                .map(t -> repository.save(SizeEntity.builder()
                        .size(Size.valueOf(size.getSize()))
                        .product(productRepository.findByName(size.getSizeProduct()))
                        .updatedAt(LocalDateTime.now())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Save Product failed"));
    }

    @Override
    public SizeEntity update(String token, SizeModel size) {
        log.info("Update Size started");
        return validateUser(token)
                .filter(t -> {
                    boolean exists = repository.existsById(size.getId());
                    if (!exists) {
                        log.error("Not found product: {}", size.getId());
                    }
                    return exists;
                })
                .map(t -> repository.save(SizeEntity.builder()
                        .id(size.getId())
                        .size(Size.valueOf(size.getSize()))
                        .product(productRepository.findByName(size.getSizeProduct()))
                        .updatedAt(LocalDateTime.now())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Update Size failed: Size not found"));
    }

    @Override
    public void deleteById(String token, Long id) {
        log.info("Delete Size started");
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
