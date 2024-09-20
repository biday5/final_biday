package shop.biday.model.repository;

import shop.biday.model.entity.WishEntity;

public interface QWishRepository {

    void deleteWish(Long userId, Long productId);

    WishEntity findByUserIdAndProductId(Long userId, Long productId);

}
