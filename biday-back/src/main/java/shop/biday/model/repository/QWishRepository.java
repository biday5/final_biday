package shop.biday.model.repository;

import shop.biday.model.entity.WishEntity;

import java.util.List;

public interface QWishRepository {

    void deleteWish(String email, Long productId);

    WishEntity findByEmailAndProductId(String email, Long productId);

    List<?> findByUserEmail(String email);

}
