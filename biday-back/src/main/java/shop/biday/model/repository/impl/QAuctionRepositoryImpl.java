package shop.biday.model.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.dto.AwardDto;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.dto.SizeDto;
import shop.biday.model.entity.*;
import shop.biday.model.repository.QAuctionRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QAuctionRepositoryImpl implements QAuctionRepository {
    private final JPAQueryFactory queryFactory;

    private final QAuctionEntity qAuction = QAuctionEntity.auctionEntity;
    private final QProductEntity qProduct = QProductEntity.productEntity;
    private final QBrandEntity qBrand = QBrandEntity.brandEntity;
    private final QCategoryEntity qCategory = QCategoryEntity.categoryEntity;
    private final QAwardEntity qAward = QAwardEntity.awardEntity;
    private final QSizeEntity qSize = QSizeEntity.sizeEntity;
    private final QWishEntity qWish = QWishEntity.wishEntity;

    // TODO : USER Q클래스 사용할 수 없기 때문에, 그냥 UserDto 클래스 호출해서 Id랑 사진만 담아서 보내지는지 확인 할 것
    @Override
    public AuctionModel findByAuctionId(Long id) {
        AuctionModel auction = queryFactory
                .select(Projections.constructor(AuctionModel.class,
                        qAuction.id,
//                        (Expression<?>) Projections.constructor(UserDto.class,
//                                qAuction.user,
//                                "user_name", // 이게 과연 될까???
//                                createDefaultImageProjection()),
                        qAuction.userId,
                        createSizeDtoProjection(),
                        qAuction.description,
                        qAuction.startingBid,
                        qAuction.currentBid,
                        qAuction.startedAt,
                        qAuction.endedAt,
                        qAuction.status,
                        qAuction.createdAt,
                        qAuction.updatedAt,
                        Projections.list(createDefaultImageProjection()),
                        Projections.constructor(AwardDto.class,
                                qAward.id,
                                qAward.auction.id.as("auction"),
                                qAward.userId,
                                qAward.bidedAt,
                                qAward.currentBid,
                                qAward.count)))
                .from(qAuction)
                .leftJoin(qAuction.size, qSize)
                .leftJoin(qSize.product, qProduct)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qAuction.award, qAward)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .where(qAuction.id.eq(id))
                .fetchFirst();

        return auction;
    }

    @Override
    public Slice<AuctionDto> findByUser(String user, String period, Long cursor, Pageable pageable) {
        LocalDateTime startDate = switch (period) {
            case "3개월" -> LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
            case "6개월" -> LocalDateTime.now().minus(6, ChronoUnit.MONTHS);
            case "12개월" -> LocalDateTime.now().minus(12, ChronoUnit.MONTHS);
            case "전체보기" -> null;
            default -> throw new IllegalArgumentException("Invalid period specified");
        };

        // 날짜 범위 조건 설정
        BooleanExpression datePredicate = startDate != null ? qAuction.startedAt.goe(startDate) : null;

        // 커서 기반 조건 설정
        BooleanExpression cursorPredicate = cursor != null ? qAuction.id.lt(cursor) : null;

        // QueryDSL 쿼리 빌더
        List<AuctionDto> auctions = queryFactory
                .select(createAuctionDtoProjection())
                .from(qAuction)
                .leftJoin(qAuction.size, qSize)
                .leftJoin(qSize.product, qProduct)
                .where(qAuction.userId.eq(user)
                        .and(datePredicate)
                        .and(cursorPredicate))
                .orderBy(qAuction.startedAt.desc())
                .fetch();

        return createSlice(auctions, pageable);
    }

    @Override
    public Slice<AuctionDto> findByProduct(Long sizeId, String order, Long cursor, Pageable pageable) {
        // TODO productId 같이 내려줘야 할 듯
        BooleanExpression sizePredicate = sizeId != null ? qAuction.size.id.eq(sizeId) : null;
        BooleanExpression cursorPredicate = cursor != null ? qAuction.id.lt(cursor) : null;

        OrderSpecifier<?> datePredicate = switch (order) {
            case "종료 임박 순" -> qAuction.endedAt.asc();
            case "시작 순" -> qAuction.startedAt.asc();
            default -> qAuction.startedAt.asc();
        };

        List<AuctionDto> auctions = queryFactory
                .select(createAuctionDtoProjection())
                .from(qAuction)
                .leftJoin(qAuction.size, qSize)
                .leftJoin(qSize.product, qProduct)
                .where(
                        sizePredicate,
                        cursorPredicate,
                        qAuction.status.eq(false),
                        qAuction.endedAt.goe(LocalDateTime.now())
                )
                .orderBy(datePredicate)
                .fetch();

        return createSlice(auctions, pageable);
    }

    private JPQLQuery<Long> auctionCount() {
        return JPAExpressions
                .select(qAuction.count().coalesce(0L))
                .from(qAuction)
                .where(qAuction.status.eq(false),
                        qAuction.size.product.id.eq(qProduct.id));
    }

    private JPQLQuery<Long> wishCount() {
        return JPAExpressions
                .select(qWish.count().coalesce(0L))
                .from(qWish)
                .where(qWish.product.id.eq(qProduct.id));
    }

    private ConstructorExpression<SizeDto> createSizeDtoProjection() {
        return Projections.constructor(SizeDto.class,
                qSize.id,
                createProductDtoProjection(),
                qSize.size.stringValue());
    }

    private ConstructorExpression<ProductDto> createProductDtoProjection() {
        return Projections.constructor(ProductDto.class,
                qProduct.id,
                qBrand.name.as("brand"),
                qCategory.name.as("category"),
                qProduct.name,
                qProduct.subName,
                qProduct.productCode,
                qProduct.price,
                qProduct.color,
                createDefaultImageProjection(),
                auctionCount(),
                wishCount()
        );
    }

    private ConstructorExpression<ImageModel> createDefaultImageProjection() {
        return Projections.constructor(ImageModel.class,
                Expressions.constant("defaultImageId"),
                Expressions.constant("defaultImageOriginalName"),
                Expressions.constant("defaultImageUploadName"),
                Expressions.constant("defaultImageUploadName"),
                Expressions.constant("defaultImageUploadUrl"),
                Expressions.constant("defaultImageType"),
                Expressions.constant(0L),
                Expressions.constant(LocalDateTime.now())
        );
    }

    private ConstructorExpression<AuctionDto> createAuctionDtoProjection() {
        return Projections.constructor(AuctionDto.class,
                qAuction.id,
                qAuction.userId,
                qProduct.name.as("product"),
                qSize.size.stringValue(),
                qAuction.startingBid,
                qAuction.currentBid,
                qAuction.startedAt,
                qAuction.endedAt,
                qAuction.status,
                qAuction.createdAt,
                qAuction.updatedAt);
    }

    private Slice<AuctionDto> createSlice(List<AuctionDto> auctions, Pageable pageable) {
        boolean hasNext = auctions.size() > pageable.getPageSize();
        if (hasNext) {
            auctions.remove(auctions.size() - 1);
        }
        return new SliceImpl<>(auctions, pageable, hasNext);
    }
}
