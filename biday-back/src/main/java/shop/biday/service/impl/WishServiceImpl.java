package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.entity.WishEntity;
import shop.biday.model.repository.UserRepository;
import shop.biday.model.repository.WishRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.WishService;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Override
    public boolean toggleWIsh(String email, Long productId) {

        return isWish(email, productId) ? deleteWishAndReturnFalse(email, productId) : insertWishAndReturnTrue(email, productId);

    }

    private boolean deleteWishAndReturnFalse(String email, Long productId) {
        deleteWish(email, productId);
        return false;
    }

    private boolean insertWishAndReturnTrue(String email, Long productId) {
        insertWish(email, productId);
        return true;
    }

    @Override
    public void deleteWish(String email, Long productId) {
        wishRepository.deleteWish(email, productId);
    }

    @Override
    public void insertWish(String email, Long productId) {

        UserEntity user = userRepository.findByEmail(email);

        wishRepository.save(
                WishEntity.builder()
                        .user(UserEntity.builder().id(user.getId()).build())
                        .product(ProductEntity.builder().id(productId).build())
                        .build()
        );

    }

    @Override
    public boolean isWish(String email, Long productId) {

        return wishRepository.findByEmailAndProductId(email, productId) != null;

    }
}