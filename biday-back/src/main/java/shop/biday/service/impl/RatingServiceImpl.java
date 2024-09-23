package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.RatingModel;
import shop.biday.model.entity.RatingEntity;
import shop.biday.model.repository.QRatingRepository;
import shop.biday.model.repository.RatingRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.RatingService;
import shop.biday.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository repository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public List<RatingEntity> findBySeller(String token, Long userId) {
        log.info("Find All Rating By Seller : {}", userId);
        return validateUser(token)
                .filter(t-> {
                    boolean exists = userRepository.existsById(userId);
                    if(!exists) {
                        log.error("User doesn't exist : {}", userId);
                    }
                    return exists;
                })
                .map(t-> repository.findBySeller(userId))
                .orElse(null);
    }

    @Override
    public RatingEntity save(String token, RatingModel ratingModel) {
        log.info("Save Rating started");
        return validateUser(token)
                .map(t->repository.save(ratingModel))
                .orElse(null);
    }

    private Optional<String> validateUser(String token) {
        log.info("Validate User started for token: {}", token);
        return Optional.of(token)
                .filter(t -> jwtUtil.getRole(t).equalsIgnoreCase("ROLE_SELLER"))
                .filter(t -> userRepository.existsByEmail(jwtUtil.getEmail(t)))
                .or(() -> {
                    log.error("User does not have role SELLER or does not exist for token: {}", token);
                    return Optional.empty();
                });
    }
}
