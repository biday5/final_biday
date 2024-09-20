package shop.biday.model.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.QWishEntity;
import shop.biday.model.entity.WishEntity;
import shop.biday.model.repository.QWishRepository;

@Repository
@RequiredArgsConstructor
public class QWishRepositoryImpl implements QWishRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public void deleteWish(Long userId, Long productId) {
        QWishEntity wishEntity = QWishEntity.wishEntity;


        queryFactory.delete(wishEntity)
                .where(wishEntity.user.id.eq(userId)
                        .and(wishEntity.product.id.eq(productId)))
                .execute();
    }

    @Override
    public WishEntity findByUserIdAndProductId(Long userId, Long productId) {
        QWishEntity wishEntity = QWishEntity.wishEntity;

        queryFactory.select(wishEntity)
                .where(wishEntity.user.id.eq(userId)
                        .and(wishEntity.product.id.eq(productId)));


        return null;
    }
}
