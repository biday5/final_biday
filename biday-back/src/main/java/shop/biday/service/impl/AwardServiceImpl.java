package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.dto.AwardDto;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.entity.AwardEntity;
import shop.biday.model.repository.AwardRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.AwardService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {

    private final AwardRepository awardRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public List<AwardEntity> findAll() {
        return awardRepository.findAll();
    }

    @Override
    public AwardEntity findById(Long id) {
        return awardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 데이터입니다."));
    }

    @Override
    public AwardEntity save(AwardEntity award) {
        return awardRepository.save(award);
    }

    @Override
    public AwardModel findByAwardId(String token, Long id) {
        log.info("Find award by id: {}", id);
        return validateUser(token)
                .filter(t -> {
                    boolean exists = awardRepository.existsById(id);
                    if (!exists) {
                        log.error("Award does not exist for id: {}", id);
                    }
                    return exists;
                })
                .map(t -> awardRepository.findByAwardId(id))
                .orElse(null);
    }

    @Override
    public Slice<AwardDto> findByUser(String token, Long userId, String period, LocalDateTime cursor, Pageable pageable) {
        log.info("Find awards by UserId: {}", userId);
        return validateUser(token)
                .filter(t -> {
                    boolean exists = userRepository.existsById(userId);
                    if (!exists) {
                        log.error("User does not exist for userId: {}", userId);
                    }
                    return exists;
                })
                .map(t -> awardRepository.findByUserId(userId, period, cursor, pageable))
                .orElse(null);
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
