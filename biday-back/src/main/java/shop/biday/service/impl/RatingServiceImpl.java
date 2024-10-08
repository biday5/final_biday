package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.RatingModel;
import shop.biday.model.entity.RatingEntity;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.repository.PaymentRepository;
import shop.biday.model.repository.RatingRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.RatingService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final PaymentRepository paymentService;

    @Override
    public double findSellerRate(String sellerId) {
        log.info("Calculate Rate by Seller: {}", sellerId);
        if (userRepository.existsById(Long.valueOf(sellerId))) {
            return ratingRepository.findSellerRating(sellerId);
        } else {
            log.error("User doesn't exist : {}", sellerId);
            return 0;
        }
    }

    @Override
    public List<RatingEntity> findBySeller(String token, String sellerId) {
        // TODO : repository mongo로 바뀌고 나서 처리 할 것
        log.info("Find All Rating By Seller : {}", sellerId);
        return validateUser(token)
                .filter(t -> {
                    boolean exists = userRepository.existsById(Long.valueOf(sellerId));
                    if (!exists) {
                        log.error("User doesn't exist : {}", sellerId);
                    }
                    return exists;
                })
                .map(t -> ratingRepository.findBySeller(sellerId))
                .orElseThrow(() -> new RuntimeException("Find Rating by User failed"));
    }

    @Override
    public RatingEntity save(String token, RatingModel ratingModel) {
        log.info("Save Rating started");

        return validateUser(token)
                .flatMap(user -> {
                    boolean exists = paymentService.existsById(ratingModel.getPaymentId());
                    if (!exists) {
                        log.error("Payment doesn't exist: {}", ratingModel.getPaymentId());
                        return Optional.empty();
                    }
                    log.debug("Find Payment Success: {}", ratingModel.getPaymentId());

                    RatingEntity ratingEntity = RatingEntity.builder()
                            .paymentId(ratingModel.getPaymentId())
                            .sellerId(ratingModel.getSellerId())
                            .rating(ratingModel.getRating())
                            .build();
                    ratingRepository.save(ratingEntity);

                    UserEntity userEntity = userRepository.findByEmail(ratingEntity.getSellerId());
                    if (userEntity != null) {
                        userEntity.setTotalRating(ratingRepository.findSellerRating(userEntity.getEmail()));
                        userRepository.save(userEntity);
                        log.debug("User {} Rate Updated: {}", userEntity.getName(), ratingEntity.getRating());
                    } else {
                        log.warn("User not found: {}", ratingEntity.getSellerId());
                    }

                    return Optional.of(ratingEntity);
                })
                .orElseThrow(() -> new RuntimeException("Failed to save rating: Payment ID " + ratingModel.getPaymentId() + " does not exist"));
    }


    // TODO user id 기준으로 찾기!! 나중에 한번에 수정
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
