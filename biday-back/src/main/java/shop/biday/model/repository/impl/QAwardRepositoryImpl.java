package shop.biday.model.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.domain.PaymentTempModel;
import shop.biday.model.domain.UserModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.dto.AwardDto;
import shop.biday.model.entity.*;
import shop.biday.model.repository.QAwardRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QAwardRepositoryImpl implements QAwardRepository {
    private final JPAQueryFactory queryFactory;

    private final QAwardEntity qAward = QAwardEntity.awardEntity;
    private final QAuctionEntity qAuction = QAuctionEntity.auctionEntity;
    private final QProductEntity qProduct = QProductEntity.productEntity;
    private final QUserEntity qUser = QUserEntity.userEntity;

    @Override
    public AwardModel findByAwardId(Long id) {
        return queryFactory
                .select(Projections.constructor(AwardModel.class,
                        qAward.id,
                        Projections.constructor(AuctionDto.class,
                                qAuction.id,
                                qAuction.userId,
                                qProduct.name.as("product"),
                                qAuction.startingBid,
                                qAuction.currentBid,
                                qAuction.startedAt,
                                qAuction.endedAt,
                                qAuction.status,
                                qAuction.createdAt,
                                qAuction.updatedAt
                        ),
                        qUser.name.as("user"),
                        qAward.bidedAt,
                        qAward.currentBid,
                        qAward.count
                ))
                .from(qAward)
                .leftJoin(qAward.auction, qAuction)
                .leftJoin(qAuction.product, qProduct)
                .leftJoin(qAward.user, qUser)
                .where(qAward.id.eq(id))
                .fetchOne();
    }

    @Override
    public Slice<AwardDto> findByUserId(Long userId, String period, LocalDateTime cursor, Pageable pageable) {
        LocalDateTime startDate = switch (period) {
            case "3개월" -> LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
            case "6개월" -> LocalDateTime.now().minus(6, ChronoUnit.MONTHS);
            case "12개월" -> LocalDateTime.now().minus(12, ChronoUnit.MONTHS);
            case "전체보기" -> null;
            default -> throw new IllegalArgumentException("Invalid period specified");
        };

        // 날짜 범위 조건 설정
        BooleanExpression datePredicate = startDate != null ? qAward.bidedAt.goe(startDate) : null;

        // 커서 기반 조건 설정
        BooleanExpression cursorPredicate = cursor != null ? qAward.bidedAt.lt(cursor) : null;

        // QueryDSL 쿼리 빌더
        List<AwardDto> auctions = queryFactory
                .select(Projections.constructor(AwardDto.class,
                        qAward.id,
                        qAuction.id.as("auction"),
                        qUser.name.as("user"),
                        qAward.bidedAt,
                        qAward.currentBid,
                        qAward.count))
                .from(qAward)
                .leftJoin(qAward.auction, qAuction)
                .leftJoin(qAuction.product, qProduct)
                .leftJoin(qAward.user, qUser)
                .where(qAward.user.id.eq(userId)
                        .and(datePredicate)
                        .and(cursorPredicate))
                .orderBy(qAward.bidedAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = auctions.size() > pageable.getPageSize();
        if (hasNext) {
            auctions.remove(auctions.size() - 1);
        }

        return new SliceImpl<>(auctions, pageable, hasNext);
    }
}
