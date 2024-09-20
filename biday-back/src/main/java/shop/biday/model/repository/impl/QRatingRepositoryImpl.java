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
    public List<RatingEntity> findBySeller(Long userId) {
        return queryFactory
                .selectFrom(qRating)
                .where(qRating.userId.eq(userId))
                .fetch();
    }
}
