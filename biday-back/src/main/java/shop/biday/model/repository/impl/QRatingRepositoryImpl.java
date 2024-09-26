package shop.biday.model.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.QRatingEntity;
import shop.biday.model.entity.RatingEntity;
import shop.biday.model.repository.QRatingRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QRatingRepositoryImpl implements QRatingRepository {
    private final JPAQueryFactory queryFactory;

    private final QRatingEntity qRating = QRatingEntity.ratingEntity;

    @Override
    public List<RatingEntity> findBySeller(String sellerId) {
        return queryFactory
                .selectFrom(qRating)
                .where(qRating.sellerId.eq(sellerId))
                .fetch();
    }

    // TODO sellerId 기준으로 해서 rate avg() 해서 가져오게 하기
    @Override
    public double findSellerRating(String sellerId) {
        return queryFactory
                .select(qRating.rate.avg())
                .from(qRating)
                .where(qRating.sellerId.eq(sellerId))
                .fetch();
    }
}
