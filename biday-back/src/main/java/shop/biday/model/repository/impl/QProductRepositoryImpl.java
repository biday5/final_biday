package shop.biday.model.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.*;
import shop.biday.model.repository.QProductRepository;
import shop.biday.service.ImageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QProductRepositoryImpl implements QProductRepository {
    private final JPAQueryFactory queryFactory;

    private final QProductEntity qProduct = QProductEntity.productEntity;
    private final QBrandEntity qBrand = QBrandEntity.brandEntity;
    private final QCategoryEntity qCategory = QCategoryEntity.categoryEntity;
    private final QSizeEntity qSize = QSizeEntity.sizeEntity;
    private final QAuctionEntity qAuction = QAuctionEntity.auctionEntity;
    private final QWishEntity qWish = QWishEntity.wishEntity;

    private final ImageService imageService;

    // auction 수 이상하게 나옴
    @Override
    public Slice<ProductDto> findProducts(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemValue) {
        List<ProductDto> list = createBaseQuery(queryFactory, pageable, categoryId, brandId, keyword, color, order, lastItemValue)
                .fetch();

        boolean hasNext = list.size() > pageable.getPageSize();
        if (hasNext) {
            list = list.subList(0, pageable.getPageSize());
        }

        if (!list.isEmpty()) {
//            for (ProductDto productDto : list) {
//                ImageModel imageModel = imageService.findByOriginalNameAndTypeAndReferencedId(productDto.getProductCode(), "상품", productDto.getId());
//                if (imageModel != null) {
//                    productDto.setImage(imageModel);
//                    log.debug("Product Image Found : {}", imageModel.getOriginalName());
//                } else {
//                    productDto.setImage(imageService.findByTypeAndUploadPath("에러", "error"));
//                    log.debug("Product Image Not Found : {}", imageService.findByTypeAndUploadPath("에러", "error").getOriginalName());
//                }
//            }
        }

        return new SliceImpl<>(list, pageable, hasNext);
    }

    @Override
    public Map<Long, ProductModel> findByProductId(Long id, String name) {
        return queryFactory
                .selectFrom(qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .leftJoin(qSize).on(qSize.product.id.eq(qProduct.id))
                .where(qProduct.id.eq(id).or(qProduct.name.containsIgnoreCase(name)))
                .orderBy(qSize.id.asc())
                .transform(groupBy(qProduct.id).as(Projections.constructor(ProductModel.class,
                        qProduct.id,
                        qBrand.name.as("brand"),
                        qCategory.name.as("category"),
                        qProduct.name,
                        qProduct.subName,
                        qProduct.productCode,
                        qProduct.price,
                        qProduct.color,
                        qProduct.description,
                        qProduct.createdAt,
                        qProduct.updatedAt,
                        wishCount(),
//                        createDefaultImageProjection(),
                        set(createDefaultSizeProjection())
                )));
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
//                createDefaultImageProjection(),
                wishCount(),
                auctionCount()
        );
    }

    private JPQLQuery<Long> auctionCount() {
        return JPAExpressions.select(qAuction.count().coalesce(0L))
                .from(qAuction)
                .where(
                        qAuction.status.eq(false),
                        qAuction.size.product.id.eq(qSize.product.id)
                );
    }

    private JPQLQuery<Long> wishCount() {
        return JPAExpressions.select(qWish.count().coalesce(0L))
                .from(qWish)
                .where(qWish.product.id.eq(qProduct.id));
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
                Expressions.constant(LocalDateTime.now()),
                Expressions.constant(LocalDateTime.now())
        );
    }

    private ConstructorExpression<SizeModel> createDefaultSizeProjection() {
        return Projections.constructor(SizeModel.class,
                qSize.id,
                qProduct.name.as("sizeProduct"),
                qSize.size.stringValue(),
                qSize.createdAt,
                qSize.updatedAt
        );
    }

    private JPQLQuery<ProductDto> createBaseQuery(JPAQueryFactory queryFactory, Pageable pageable, Long categoryId, Long brandId, String keyword,
                                                  String color, String order, Long lastItemId) {
        JPQLQuery<ProductDto> query = queryFactory
                .select(createProductDtoProjection())
                .from(qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .leftJoin(qProduct.sizes, qSize)
                .leftJoin(qSize.auctions, qAuction)
                .where(
                        findByBrand(brandId),
                        findByCategory(categoryId),
                        findByColor(color),
                        findByKeyword(keyword)
                );

        findByOrdering(query, order, lastItemId);

        return query
                .groupBy(qProduct.id, qBrand.name, qCategory.name, qProduct.name, qProduct.subName, qProduct.productCode, qProduct.price, qProduct.color)
                .limit(pageable.getPageSize() + 1); // 페이지 사이즈 + 1을 반환하여 더 많은 데이터가 있는지 확인
    }

    private BooleanExpression findByCategory(Long categoryId) {
        return categoryId != null ? qCategory.id.eq(categoryId) : null;
    }

    private BooleanExpression findByBrand(Long brandId) {
        return brandId != null ? qBrand.id.eq(brandId) : null;
    }

    private BooleanExpression findByColor(String color) {
        return color != null ? qProduct.color.containsIgnoreCase(color) : null;
    }

    private BooleanExpression findByKeyword(String keyword) {
        return keyword != null ? qProduct.name.containsIgnoreCase(keyword)
                .or(qProduct.subName.containsIgnoreCase(keyword))
                .or(qProduct.productCode.containsIgnoreCase(keyword))
                .or(qProduct.color.containsIgnoreCase(keyword))
                .or(qCategory.name.containsIgnoreCase(keyword))
                .or(qBrand.name.containsIgnoreCase(keyword)) : null;
    }

    private void findByOrdering(JPQLQuery<ProductDto> query, String order, Long lastItemId) {
        switch (order) {
            // TODO orderBy 제대로 안 먹음
            case "경매 적은 순":
                if (lastItemId != null) {
                    query.where(auctionCount().gt(fetchAuctionByCount(lastItemId)));
                }
                query.orderBy(qAuction.count().isNotNull().asc());
                break;
            case "경매 많은 순":
                if (lastItemId != null) {
                    query.where(auctionCount().lt(fetchAuctionByCount(lastItemId)));
                }
                query.orderBy(qAuction.count().desc());
                break;
            case "가격 낮은 순":
                if (lastItemId != null) {
                    query.where(qProduct.price.gt(fetchProductByPrice(lastItemId)));
                }
                query.orderBy(qProduct.price.asc());
                break;
            case "가격 높은 순":
                if (lastItemId != null) {
                    query.where(qProduct.price.lt(fetchProductByPrice(lastItemId)));
                }
                query.orderBy(qProduct.price.desc());
                break;
            case "위시 적은 순":
                if (lastItemId != null) {
                    query.where(qWish.count().gt(fetchWishByCount(lastItemId)));
                }
                query.orderBy(qWish.count().asc());
                break;
            case "위시 많은 순":
                if (lastItemId != null) {
                    query.where(qWish.count().lt(fetchWishByCount(lastItemId)));
                }
                query.orderBy(qWish.count().desc());
                break;// 리스트 기준 바꾸기
            default:
                if (lastItemId != null) {
                    query.where(qProduct.id.gt(lastItemId));
                }
                query.orderBy(qProduct.id.asc());
                break;
        }
    }

    private Long fetchProductByPrice(Long id) {
        return queryFactory
                .select(qProduct.price.coalesce(0L))
                .from(qProduct)
                .where(qProduct.id.eq(id))
                .fetchOne();
    }

    private Long fetchAuctionByCount(Long id) {
        return queryFactory
                .select(qAuction.count().coalesce(0L))
                .from(qAuction)
                .leftJoin(qAuction.size, qSize)
                .where(qAuction.status.eq(false),
                        qAuction.size.product.id.eq(id))
                .fetchOne();
    }


    private Long fetchWishByCount(Long id) {
        return queryFactory
                .select(qWish.count().coalesce(0L))
                .from(qProduct)
                .where(qWish.product.id.eq(id))
                .fetchOne();
    }
}