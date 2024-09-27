package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.dto.UserDto;
import shop.biday.model.entity.AuctionEntity;
import shop.biday.model.repository.AuctionRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.AuctionService;
import shop.biday.service.ImageService;
import shop.biday.service.RatingService;
import shop.biday.service.SizeService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private static final String ROLE_SELLER = "ROLE_SELLER";

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final ImageService imageService;
    private final SizeService sizeService;
    private final RatingService ratingService;

    @Override
    public AuctionModel findById(Long id) {
        log.info("Find Auction by id: {}", id);
        return Optional.ofNullable(auctionRepository.findByAuctionId(id))
                .map(auction -> {
                    UserDto user = auction.getUser();
                    int rate = (int) ratingService.findSellerRate(String.valueOf(user.getId()));
                    ImageModel userImage = (rate == 0)
                            ? imageService.findByOriginalNameAndType(String.valueOf(rate), "평점")
                            : imageService.findByOriginalNameAndType("2", "평점");
                    user.setImage(userImage);

                    ProductDto product = auction.getSize().getProduct();
                    ImageModel productImage = imageService.findByOriginalNameAndType(product.getProductCode(), "상품");
                    log.debug("Found ProductImage: {}", productImage);
                    product.setImage(productImage != null ? productImage : imageService.findByTypeAndUploadPath("에러", "error"));

                    List<ImageModel> images = imageService.findByTypeAndReferencedId("경매", auction.getId());
                    log.debug("Found Images: {}", images);
                    auction.setImages(images != null ? images : (List<ImageModel>) imageService.findByTypeAndUploadPath("에러", "error"));

                    return auction;
                })
                .orElseGet(() -> {
                    log.warn("Auction not found for id: {}", id);
                    return null;
                });
    }

    @Override
    public boolean existsById(Long id) {
        log.info("Exists Auction by id: {}", id);
        return auctionRepository.existsById(id);
    }

    @Override
    public Slice<AuctionDto> findByProduct(Long sizeId, String order, Long cursor, Pageable pageable) {
        log.info("Find All Auctions By Time: {} SizeId: {}", order, sizeId);
        return auctionRepository.findByProduct(sizeId, order, cursor, pageable);
    }

    // TODO user 호출할 때 id 기준으로 바꿀 것
    @Override
    public Slice<AuctionDto> findByUser(String token, String user, String period, Long cursor, Pageable pageable) {
        log.info("Find All Auctions By User: {}", user);
        return validateUser(token)
                .filter(t -> userRepository.existsByEmail(user))
                .map(t -> auctionRepository.findByUser(user, period, cursor, pageable))
                .orElseThrow(() -> new IllegalArgumentException("User does not exist or invalid token"));
    }

    @Override
    public AuctionEntity updateState(Long id) {
        log.info("Update Auction Status by id: {}", id);
        return auctionRepository.findById(id).map(auction -> {
            auction.setStatus(true);
            return auctionRepository.save(auction);
        }).orElseThrow(() -> new NoSuchElementException("Auction not found with id: " + id));
    }

    @Override
    public AuctionEntity save(String token, AuctionModel auction) {
        log.info("Save Auction started");
        return validateUser(token)
                .map(t -> {
                    setStartingBid(auction);
                    return auctionRepository.save(AuctionEntity.builder()
                            .userId(auction.getUser().getId())
                            .size(sizeService.findBySize(auction.getSize().getSize()))
                            .description(auction.getDescription())
                            .startingBid(auction.getStartingBid())
                            .currentBid(auction.getCurrentBid())
                            .startedAt(auction.getStartedAt())
                            .endedAt(auction.getEndedAt())
                            .build());
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid token or not a seller"));
    }

    @Override
    public AuctionEntity update(String token, AuctionModel auction) {
        log.info("Update Auction started");
        return validateUser(token)
                .filter(t -> {
                    boolean exists = auctionRepository.existsById(auction.getId());
                    if (!exists) {
                        log.error("Not found auction: {}", auction.getId());
                    }
                    return exists;
                })
                .map(t -> auctionRepository.save(AuctionEntity.builder()
                        .userId(auction.getUser().getId())
                        .size(sizeService.findBySize(auction.getSize().getSize()))
                        .description(auction.getDescription())
                        .startingBid(auction.getStartingBid())
                        .currentBid(auction.getCurrentBid())
                        .startedAt(auction.getStartedAt())
                        .endedAt(auction.getEndedAt())
                        .build()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid token or not a seller"));
    }

    @Override
    public String deleteById(String token, Long id) {
        log.info("Delete Auction started for id: {}", id);

        return validateUser(token).map(t -> {
            if (!auctionRepository.existsById(id)) {
                log.error("Auction does not exist for id: {}", id);
                return "경매 삭제 실패";
            }

            String auctionId = auctionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Auction not found"))
                    .getUserId();
            String requestingUserId = jwtUtil.getEmail(token);

            if (!auctionId.equals(requestingUserId)) {
                log.error("User does not have Delete Authority for auction id: {}", id);
                return "삭제 권한이 없습니다"; // Returning an error message
            } else {
                auctionRepository.deleteById(id);
                log.info("Delete Auction By User for id: {}", id);
                return "경매 삭제 성공";
            }
        }).orElseGet(() -> {
            log.error("User does not have role SELLER or does not exist");
            return "유효하지 않은 사용자"; // Returning an error message
        });
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
        if (auction.getSize().getProduct().getPrice() / 2 != auction.getStartingBid()) {
            auction.setStartingBid(auction.getSize().getProduct().getPrice() / 2);
        }
    }
}
