package shop.biday.model.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.domain.BidModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.dto.BidDto;
import shop.biday.model.entity.QAuctionEntity;
import shop.biday.model.entity.QBidEntity;
import shop.biday.model.entity.QProductEntity;
import shop.biday.model.repository.QBidRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QBidRepositoryImpl implements QBidRepository {
    private final JPAQueryFactory queryFactory;

    private final QBidEntity qBid = QBidEntity.bidEntity;
    private final QAuctionEntity qAuction = QAuctionEntity.auctionEntity;
    private final QProductEntity qProduct = QProductEntity.productEntity;

    @Override
    public BidModel findById(Long id) {
        // TODO 경매, 상품, 결제, 배송 다 조인해서 보여주기 -> 결제랑 배송은 보류,
        // 람다, responseEntity 변경, Exception 처리, image, 결제 합치기
        return queryFactory
                .select(Projections.constructor(BidModel.class,
                        qBid.id,
                        Projections.constructor(AuctionDto.class,
                                qAuction.id,
                                qAuction.userId,
                                qProduct.name.as("product"),
                                qAuction.startingBid,
                                qAuction.startedAt,
                                qAuction.endedAt,
                                qAuction.status,
                                qAuction.createdAt,
                                qAuction.updatedAt
                        ),
                        qBid.userId,
                        qBid.bidedAt,
                        qBid.currentBid,
                        qBid.count,
                        qBid.createdAt,
                        qBid.award))
                .from(qBid)
                .leftJoin(qBid.auction, qAuction)
                .leftJoin(qAuction.product, qProduct)
                .where(qBid.id.eq(id))
                .fetchOne();
    }

    @Override
    public Slice<BidDto> findByUserId(Long userId, String period, LocalDateTime cursor, Pageable pageable) {
        // 현재 날짜와 시간
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = null;

        // period에 따라 날짜 범위를 설정
        switch (period) {
            case "3개월":
                startDate = now.minus(3, ChronoUnit.MONTHS);
                break;
            case "6개월":
                startDate = now.minus(6, ChronoUnit.MONTHS);
                break;
            case "12개월":
                startDate = now.minus(12, ChronoUnit.MONTHS);
                break;
            case "전체보기":
                startDate = null;
                break;
            default:
                throw new IllegalArgumentException("Invalid period specified");
        }

        // 날짜 범위 조건 설정
        BooleanExpression datePredicate = startDate != null ? qBid.bidedAt.goe(startDate) : null;

        // 커서 기반 조건 설정
        BooleanExpression cursorPredicate = cursor != null ? qBid.bidedAt.lt(cursor) : null;

        // QueryDSL 쿼리 빌더
        List<BidDto> auctions = queryFactory
                .select(Projections.constructor(BidDto.class,
                        qBid.id,
                        qAuction.id.as("auction"),
                        qBid.userId,
                        qBid.bidedAt,
                        qBid.currentBid,
                        qBid.award))
                .from(qBid)
                .leftJoin(qBid.auction, qAuction)
                .leftJoin(qAuction.product, qProduct)
                .where(qBid.userId.eq(userId)
                        .and(datePredicate)
                        .and(cursorPredicate)
                        .and(qBid.award.eq(true)))
                .orderBy(qBid.bidedAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = auctions.size() > pageable.getPageSize();
        if (hasNext) {
            auctions.remove(auctions.size() - 1);
        }

        return new SliceImpl<>(auctions, pageable, hasNext);
    }
}
