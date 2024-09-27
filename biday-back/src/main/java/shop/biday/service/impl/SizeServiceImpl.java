package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.entity.SizeEntity;
import shop.biday.model.entity.enums.Size;
import shop.biday.model.repository.ProductRepository;
import shop.biday.model.repository.SizeRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.SizeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Override
    public List<SizeEntity> findAll() {
        return sizeRepository.findAll();
    }

    @Override
    public Optional<SizeEntity> findById(Long id) {
        return sizeRepository.findById(id);
    }

    @Override
    public List<SizeEntity> findAllByProductId(Long productId) {
        return sizeRepository.findAllByProductId(productId);
    }

    @Override
    public SizeEntity findBySize(String size) {
        return sizeRepository.findBySize(Size.valueOf(size));
    }

    @Override
    public SizeEntity save(String token, SizeModel size) {
        log.info("Save Size started");
        return validateUser(token)
                .map(t -> sizeRepository.save(SizeEntity.builder()
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
                    boolean exists = sizeRepository.existsById(size.getId());
                    if (!exists) {
                        log.error("Not found product: {}", size.getId());
                    }
                    return exists;
                })
                .map(t -> sizeRepository.save(SizeEntity.builder()
                        .id(size.getId())
                        .size(Size.valueOf(size.getSize()))
                        .product(productRepository.findByName(size.getSizeProduct()))
                        .updatedAt(LocalDateTime.now())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Update Size failed: Size not found"));
    }

    @Override
    public String deleteById(String token, Long id) {
        log.info("Delete Size started");

        return validateUser(token).map(t -> {
            if (!sizeRepository.existsById(id)) {
                log.error("Not found size: {}", id);
                return "사이즈 삭제 실패";
            }

            sizeRepository.deleteById(id);
            log.info("Size deleted: {}", id);
            return "사이즈 삭제 성공";
        }).orElseGet(() -> {
            log.error("User does not have role ADMIN or does not exist");
            return "유효하지 않은 사용자";
        });
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
