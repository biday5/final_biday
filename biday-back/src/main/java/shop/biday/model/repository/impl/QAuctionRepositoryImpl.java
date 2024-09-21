package shop.biday.model.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
import shop.biday.model.entity.*;
import shop.biday.model.repository.ImageRepository;
import shop.biday.model.repository.QAuctionRepository;
import shop.biday.service.ImageService;

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
    private final QUserEntity qUser = QUserEntity.userEntity;
    private final ImageService imageRepository;

    @Override
    public AuctionModel findByAuctionId(Long id) {
        AuctionModel auction = queryFactory
                .select(Projections.constructor(AuctionModel.class,
                        qAuction.id,
                        qAuction.userId,
                        Projections.constructor(ProductDto.class,
                                qProduct.id,
                                qBrand.name.as("brand"),
                                qCategory.name.as("category"),
                                qProduct.name,
                                qProduct.subName,
                                qProduct.productCode,
                                qProduct.price,
                                qProduct.color,
                                Projections.constructor(ImageModel.class,
                                        Expressions.constant("defaultImageId"),        // id
                                        Expressions.constant("defaultImageName"),      // name
                                        Expressions.constant("defaultImageExt"),       // ext
                                        Expressions.constant("defaultImageUrl"),       // url
                                        Expressions.constant("defaultImageType"),      // type
                                        Expressions.constant(0L),  // referenceId
                                        Expressions.constant(LocalDateTime.now())
                                )),
                        qAuction.description,
                        qAuction.startingBid,
                        qAuction.currentBid,
                        qAuction.startedAt,
                        qAuction.endedAt,
                        qAuction.status,
                        qAuction.createdAt,
                        qAuction.updatedAt,
                        // ImageModel 생성자 호출
                        Projections.list(Projections.constructor(ImageModel.class,
                                Expressions.constant("defaultImageId"),        // id
                                Expressions.constant("defaultImageName"),      // name
                                Expressions.constant("defaultImageExt"),       // ext
                                Expressions.constant("defaultImageUrl"),       // url
                                Expressions.constant("defaultImageType"),      // type
                                Expressions.constant(0L),  // referenceId
                                Expressions.constant(LocalDateTime.now())      // createdAt
                        )),
                        Projections.constructor(AwardDto.class,
                                qAward.id,
                                qAward.auction.id.as("auction"),
                                qAward.user.email.as("user"),
                                qAward.bidedAt,
                                qAward.currentBid,
                                qAward.count)))
                .from(qAuction)
                .leftJoin(qAuction.product, qProduct)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qAuction.award, qAward)
                .leftJoin(qAward.user, qUser)
                .where(qAuction.id.eq(id),
                        qAward.auction.id.eq(id))
                .fetchFirst();

        if (auction != null) {
            List<ImageModel> images = imageRepository.findByTypeAndReferencedId("경매", auction.getId());
            if (images != null) {
                auction.setImages(images);
            }
            else {
                auction.setImages((List<ImageModel>) imageRepository.findByType("에러"));
            }

            ImageModel productImage = imageRepository.findByNameAndType(auction.getProduct().getProductCode(), "상품");
            if (productImage != null) {
                auction.getProduct().setImage(productImage);
            } else {
                auction.getProduct().setImage(imageRepository.findByType("에러"));
            }
        }

        return auction;
    }

    @Override
    public Slice<AuctionDto> findByUser(Long userId, String period, LocalDateTime cursor, Pageable pageable) {
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
        BooleanExpression datePredicate = startDate != null ? qAuction.startedAt.goe(startDate) : null;

        // 커서 기반 조건 설정
        BooleanExpression cursorPredicate = cursor != null ? qAuction.endedAt.lt(cursor) : null;

        // QueryDSL 쿼리 빌더
        List<AuctionDto> auctions = queryFactory
                .select(Projections.constructor(AuctionDto.class,
                        qAuction.id,
                        qAuction.userId,
                        qProduct.name.as("product"),
                        qAuction.startingBid,
                        qAuction.currentBid,
                        qAuction.startedAt,
                        qAuction.endedAt,
                        qAuction.status,
                        qAuction.createdAt,
                        qAuction.updatedAt))
                .from(qAuction)
                .leftJoin(qAuction.product, qProduct)
                .where(qAuction.userId.eq(userId)
                        .and(datePredicate)
                        .and(cursorPredicate))
                .orderBy(qAuction.endedAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = auctions.size() > pageable.getPageSize();
        if (hasNext) {
            auctions.remove(auctions.size() - 1);
        }

        return new SliceImpl<>(auctions, pageable, hasNext);
    }
}
