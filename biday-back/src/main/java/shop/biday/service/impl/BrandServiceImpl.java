package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;
import shop.biday.model.repository.BrandRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.BrandService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public List<BrandEntity> findAll() {
        log.info("Find all brands");
        return brandRepository.findAll();
    }

    @Override
    public Optional<BrandEntity> findById(Long id) {
        log.info("Find brand by id: {}", id);
        return Optional.of(id)
                .filter(t -> {
                    boolean exists = brandRepository.existsById(id);
                    if (!exists) {
                        log.error("Not found brand: {}", id);
                    }
                    return exists;
                })
                .flatMap(brandRepository::findById);
    }

    @Override
    public BrandEntity save(String token, BrandModel brand) {
        log.info("Save Brand started");
        return validateUser(token)
                .map(t -> {
                    return brandRepository.save(BrandEntity.builder()
                            .name(brand.getName())
                            .build());
                })
                .orElseThrow(() -> new RuntimeException("Save Brand failed"));
    }

    @Override
    public BrandEntity update(String token, BrandModel brand) {
        log.info("Update Brand started");
        return validateUser(token)
                .filter(t -> {
                    boolean exists = brandRepository.existsById(brand.getId());
                    if (!exists) {
                        log.error("Not found brand: {}", brand.getId());
                    }
                    return exists;
                })
                .map(t -> brandRepository.save(BrandEntity.builder()
                        .id(brand.getId())
                        .name(brand.getName())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Update Brand failed: Brand not found"));
    }

    @Override
    public String deleteById(String token, Long id) {
        log.info("Delete Brand started for id: {}", id);

        return validateUser(token).map(t -> {
            if (!brandRepository.existsById(id)) {
                log.error("Not found brand: {}", id);
                return "브랜드 삭제 실패";
            }

            brandRepository.deleteById(id);
            log.info("Brand deleted: {}", id);
            return "브랜드 삭제 성공";
        }).orElseGet(() -> {
            log.error("User does not have role ADMIN or does not exist");
            return "유효하지 않은 사용자";
        });
    }

    private Optional<String> validateUser(String token) {
        /* TODO 휘재형이 뽑는거 따로 가져오게 되면 JwtClaims claims = jwtUtil.extractClaims(token); 으로 정보 담아서 String userId=claims.getUserId(); 이런식으로 userId 뽑아서 사용할 것*/
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
