package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.entity.WishEntity;
import shop.biday.model.repository.WishRepository;
import shop.biday.service.WishService;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    @Override
    public boolean toggleWIsh(Long userId, Long productId) {

        return isWish(userId, productId) ? deleteWishAndReturnFalse(userId, productId) : insertWishAndReturnTrue(userId, productId);

    }

    private boolean deleteWishAndReturnFalse(Long userId, Long productId) {
        deleteWish(userId, productId);
        return false;
    }

    private boolean insertWishAndReturnTrue(Long userId, Long productId) {
        insertWish(userId, productId);
        return true;
    }

    @Override
    public void deleteWish(Long userId, Long productId) {
        wishRepository.deleteWish(userId, productId);
    }

    @Override
    public void insertWish(Long userId, Long productId) {

        wishRepository.save(
                WishEntity.builder()
                        .user(UserEntity.builder().id(userId).build())
                        .product(ProductEntity.builder().id(productId).build())
                        .build()
        );

    }

    @Override
    public boolean isWish(Long userId, Long productId) {

        return wishRepository.findByUserIdAndProductId(userId, productId) != null;

    }
}
