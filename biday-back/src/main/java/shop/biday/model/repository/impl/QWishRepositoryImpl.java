package shop.biday.model.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.WishEntity;
import shop.biday.model.repository.QWishRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QWishRepositoryImpl implements QWishRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteWish(String email, Long productId) {

    }

    @Override
    public WishEntity findByEmailAndProductId(String email, Long productId) {
        return null;
    }

    @Override
    public List<?> findByUserEmail(String email) {
        return List.of();
    }
//    private final EntityManager entityManager;
//
//    QWishEntity wishEntity = QWishEntity.wishEntity;
//
//    @Override
//    public void deleteWish(String email, Long productId) {
//
//        queryFactory.delete(wishEntity)
//                .where(wishEntity.user.email.eq(email)
//                        .and(wishEntity.product.id.eq(productId)))
//                .execute();
//    }
//
//    @Override
//    public WishEntity findByEmailAndProductId(String email, Long productId) {
//        queryFactory.select(wishEntity)
//                .where(wishEntity.user.email.eq(email)
//                        .and(wishEntity.product.id.eq(productId)));
//
//
//        return null;
//    }
//
//    @Override
//    public List<?> findByUserEmail(String email) {
//        QUserEntity userEntity = QUserEntity.userEntity;
//
//        return queryFactory.selectFrom(wishEntity)
//                .join(wishEntity.user, userEntity)
//                .where(userEntity.email.eq(email))
//                .fetch();
//    }
}