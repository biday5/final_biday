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

        boolean isWish = isWish(userId, productId);
        if (isWish) {
            deleteWish(userId, productId);

            return false;
        } else {
            insertWish(userId, productId);

            return true;
        }
    }

    @Override
    public void deleteWish(Long userId, Long productId) {
        wishRepository.deleteWish(userId, productId);
    }

    @Override
    public void insertWish(Long userId, Long productId) {
        UserEntity user = UserEntity.builder().id(userId).build();
        ProductEntity product = ProductEntity.builder().id(productId).build();

        WishEntity wish = WishEntity.builder()
                .user(user)
                .product(product)
                .build();

        wishRepository.save(wish);
    }

    @Override
    public boolean isWish(Long userId, Long productId) {
        WishEntity result = wishRepository.findByUserIdAndProductId(userId, productId);

        if (result == null) {
            return false;
        } else return true;

    }
}
