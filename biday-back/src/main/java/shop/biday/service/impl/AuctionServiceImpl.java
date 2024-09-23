package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.repository.AuctionRepository;
import shop.biday.model.repository.ProductRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.AuctionService;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private static final String ROLE_SELLER = "ROLE_SELLER";

    private final AuctionRepository repository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final ProductRepository productRepository;
    private final ProductModel productModel;

    @Override
    public AuctionModel findById(Long id) {
        log.info("Find Auction by id: {}", id);
        return repository.findByAuctionId(id);
    }

    @Override
    public boolean existsById(Long id) {
        log.info("Exists Auction by id: {}", id);
        return repository.existsById(id);
    }

    @Override
    public Slice<AuctionDto> findByTime(String order, Long cursor, Pageable pageable) {
        log.info("Find All Auctions By Time: {}", order);
        return repository.findByTime(order, cursor, pageable);
    }

    @Override
    public Slice<AuctionDto> findByUser(String token, Long userId, String period, Long cursor, Pageable pageable) {
        log.info("Find All Auctions By User: {}", userId);
        return validateUser(token)
                .filter(t -> userRepository.existsById(userId))
                .map(t -> repository.findByUser(userId, period, cursor, pageable))
                .orElseThrow(() -> new IllegalArgumentException("User does not exist or invalid token"));
    }

    @Override
    public AuctionEntity save(String token, AuctionModel auction) {
        log.info("Save Auction started");
        return validateUser(token)
                .map(t -> {
                    setStartingBid(auction);
                    return repository.save(AuctionEntity.builder()
                            .userId(auction.getUserId())
                            .product(productRepository.findByName(auction.getProduct().getName()))
                            .description(auction.getDescription())
                            .startingBid(auction.getStartingBid())
                            .currentBid(auction.getCurrentBid())
                            .startedAt(auction.getStartedAt())
                            .endedAt(auction.getEndedAt())
                            .createdAt(LocalDateTime.now())
                            .build());
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid token or not a seller"));
    }

    @Override
    public AuctionEntity update(String token, AuctionModel auction) {
        log.info("Update Auction started");
        return validateUser(token)
                .map(t -> {
                    if (!repository.existsById(auction.getId())) {
                        log.error("Auction does not exist for id: {}", auction.getId());
                        return null;
                    }
                    setStartingBid(auction);
                    return repository.save(AuctionEntity.builder()
                            .userId(auction.getUserId())
                            .product(productRepository.findByName(auction.getProduct().getName()))
                            .description(auction.getDescription())
                            .startingBid(auction.getStartingBid())
                            .currentBid(auction.getCurrentBid())
                            .startedAt(auction.getStartedAt())
                            .endedAt(auction.getEndedAt())
                            .updatedAt(LocalDateTime.now())
                            .build());
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid token or not a seller"));
    }

    @Override
    public void deleteById(String token, Long id) {
        log.info("Delete Auction started for id: {}", id);
        validateUser(token).ifPresentOrElse(t -> {
            if (!repository.existsById(id)) {
                log.error("Auction does not exist for id: {}", id);
                return;
            }

            Long auctionUserId = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Auction not found"))
                    .getUserId();
            Long requestingUserId = userRepository.findByEmail(jwtUtil.getEmail(token)).getId();

            if (!auctionUserId.equals(requestingUserId)) {
                log.error("User does not have Delete Authority for auction id: {}", id);
            } else {
                repository.deleteById(id);
                log.info("Delete Auction By User for id: {}", id);
            }
        }, () -> log.error("User does not have role SELLER or does not exist"));
    }

    private Optional<String> validateUser(String token) {
        log.info("Validate User started for token: {}", token);
        return Optional.of(token)
                .filter(t -> jwtUtil.getRole(t).equalsIgnoreCase(ROLE_SELLER))
                .filter(t -> userRepository.existsByEmail(jwtUtil.getEmail(t)))
                .or(() -> {
                    log.error("User does not have role SELLER or does not exist for token: {}", token);
                    return Optional.empty();
                });
    }

    private void setStartingBid(AuctionModel auction) {
        if (auction.getProduct().getPrice() / 2 != auction.getStartingBid()) {
            auction.setStartingBid(auction.getProduct().getPrice() / 2);
        }
    }
}
