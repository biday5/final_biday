package shop.biday.model.repository.impl;


import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Coalesce;
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
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.*;
import shop.biday.model.repository.QProductRepository;
import shop.biday.service.ImageService;
import shop.biday.service.impl.CategoryServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QProductRepositoryImpl implements QProductRepository {
    private final JPAQueryFactory queryFactory;

    private final QProductEntity qProduct = QProductEntity.productEntity;
    private final QBrandEntity qBrand = QBrandEntity.brandEntity;
    private final QCategoryEntity qCategory = QCategoryEntity.categoryEntity;
    private final QAuctionEntity qAuction = QAuctionEntity.auctionEntity;
    private final QWishEntity qWish = QWishEntity.wishEntity;

    private final ImageService imageService;

    @Override
    public Slice<ProductDto> findProducts(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemValue) {
        List<ProductDto> list = createBaseQuery(queryFactory, pageable, categoryId, brandId, keyword, color, order, lastItemValue)
                .fetch();

        boolean hasNext = list.size() > pageable.getPageSize();
        if (hasNext) {
            list = list.subList(0, pageable.getPageSize());
        }

        if (!list.isEmpty()) {
            for (ProductDto productDto : list) {
                ImageModel imageModel = imageService.findByNameAndTypeAndReferencedId(productDto.getProductCode(), "상품", productDto.getId());
                if (imageModel != null) {
                    productDto.setImage(imageModel);
                    log.debug("Product Image Found : {}", imageModel.getName());
                } else {
                    productDto.setImage(imageService.findByType("에러"));
                    log.debug("Product Image Not Found : {}", imageService.findByType("에러").getName());
                }
            }
        }

        return new SliceImpl<>(list, pageable, hasNext);
    }

    @Override
    public ProductModel findByProductId(Long id) {
        List<AuctionDto> auctions = queryFactory
                .select(createAuctionDtoProjection())
                .from(qAuction)
                .leftJoin(qAuction.product, qProduct)
                .where(qAuction.product.id.eq(id))
                .orderBy(qAuction.status.asc(),
                        qAuction.startedAt.asc())
                .fetch();

        ProductModel product = queryFactory
                .select(Projections.constructor(ProductModel.class,
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
                        createDefaultImageProjection(),
                        Projections.list(createAuctionDtoProjection()),
                        wishCount()
                ))
                .from(qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qProduct.auctions, qAuction)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id))
                .where(qProduct.id.eq(id))
                .fetchFirst();

        if (product != null) {
            product.setAuctions(auctions); // 경매 리스트를 설정

            ImageModel imageModel = imageService.findByNameAndType(product.getProductCode(), "상품");
            if (imageModel != null) {
                product.setImage(imageModel);
                log.debug("Product Image Found : {}", imageModel.getName());
            } else {
                product.setImage(imageService.findByType("에러"));
                log.debug("Product Image Not Found : {}", imageService.findByType("에러").getName());
            }
        }

        return product;
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

    private JPQLQuery<Long> auctionCount() {
        return JPAExpressions.select(qAuction.count().coalesce(0L))
                .from(qAuction)
                .where(qAuction.status.eq(false), qAuction.product.id.eq(qProduct.id));
    }

    private JPQLQuery<Long> wishCount() {
        return JPAExpressions.select(qWish.count().coalesce(0L))
                .from(qWish)
                .where(qWish.product.id.eq(qProduct.id));
    }

    private ConstructorExpression<ImageModel> createDefaultImageProjection() {
        return Projections.constructor(ImageModel.class,
                Expressions.constant("defaultImageId"),
                Expressions.constant("defaultImageName"),
                Expressions.constant("defaultImageExt"),
                Expressions.constant("defaultImageUrl"),
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
                qAuction.startingBid,
                qAuction.currentBid,
                qAuction.startedAt,
                qAuction.endedAt,
                qAuction.status,
                qAuction.createdAt,
                qAuction.updatedAt);
    }

    private JPQLQuery<ProductDto> createBaseQuery(JPAQueryFactory queryFactory, Pageable pageable, Long categoryId, Long brandId, String keyword,
                                                  String color, String order, Long lastItemId) {
        JPQLQuery<ProductDto> query = queryFactory
                .select(createProductDtoProjection())
                .from(qProduct)
                .leftJoin(qProduct.category, qCategory)
                .leftJoin(qProduct.brand, qBrand)
                .leftJoin(qProduct.auctions, qAuction)
                .leftJoin(qWish).on(qProduct.id.eq(qWish.product.id));

        findByFilter(query, categoryId, brandId, keyword, color);
        findByOrdering(query, order, lastItemId);

        return query
                .groupBy(qProduct.id, qBrand.name, qCategory.name, qProduct.name, qProduct.subName, qProduct.productCode, qProduct.price, qProduct.color)
                .limit(pageable.getPageSize() + 1); // 페이지 사이즈 + 1을 반환하여 더 많은 데이터가 있는지 확인
    }

    private void findByFilter(JPQLQuery<ProductDto> query, Long categoryId, Long brandId, String keyword, String color) {
        if (categoryId != null) {
            query.where(qCategory.id.eq(categoryId));
        }

        if (brandId != null) {
            query.where(qProduct.brand.id.eq(brandId));
        }

        if (!keyword.isEmpty()) {
            query.where(
                    qProduct.name.containsIgnoreCase(keyword)
                            .or(qProduct.subName.containsIgnoreCase(keyword))
                            .or(qProduct.productCode.containsIgnoreCase(keyword))
                            .or(qProduct.color.containsIgnoreCase(keyword))
                            .or(qCategory.name.containsIgnoreCase(keyword))
                            .or(qBrand.name.containsIgnoreCase(keyword))
            );
        }

        if (!color.isEmpty()) {
            query.where(qProduct.color.containsIgnoreCase(color));
        }
    }

    private void findByOrdering(JPQLQuery<ProductDto> query, String order, Long lastItemId) {
        switch (order) {
            case "가격 낮은 순":
                if (lastItemId != null) {
                    query.where(qProduct.price.gt(fetchProductByPrice(lastItemId)));
                } else {
                    query.orderBy(qProduct.price.asc());
                }
                break;
            case "가격 높은 순":
                if (lastItemId != null) {
                    query.where(qProduct.price.lt(fetchProductByPrice(lastItemId)));
                } else {
                    query.orderBy(qProduct.price.desc());
                }
                break;
            case "경매 적은 순":
                if (lastItemId != null) {
                    query.where(qAuction.count().gt(fetchAuctionByCount(lastItemId)));
                } else {
                    query.orderBy(qAuction.count().asc());
                }
                break;
            case "경매 많은 순":
                if (lastItemId != null) {
                    query.where(qAuction.count().lt(fetchAuctionByCount(lastItemId)));
                } else {
                    query.orderBy(qAuction.count().desc());
                }
                break;
            case "위시 적은 순":
                if (lastItemId != null) {
                    query.where(qWish.count().gt(fetchWishByCount(lastItemId)));
                } else {
                    query.orderBy(qWish.count().asc());
                }
                break;
            case "위시 많은 순":
                if (lastItemId != null) {
                    query.where(qWish.count().lt(fetchWishByCount(lastItemId)));
                } else {
                    query.orderBy(qWish.count().desc());
                }
                break;
            default:
                if (lastItemId != null) {
                    query.where(qProduct.id.gt(lastItemId));
                } else {
                    query.orderBy(qProduct.id.asc());
                }
                break;
        }
    }

    private Long fetchProductByPrice(Long id) {
        return queryFactory
                .select(qProduct.price)
                .from(qProduct)
                .where(qProduct.id.eq(id))
                .fetchOne();
    }

    private Long fetchAuctionByCount(Long id) {
        return queryFactory
                .select(qAuction.count())
                .from(qProduct)
                .where(qAuction.product.id.eq(id))
                .fetchOne();
    }

    private Long fetchWishByCount(Long id) {
        return queryFactory
                .select(qWish.count())
                .from(qProduct)
                .where(qWish.product.id.eq(id))
                .fetchOne();
    }
}