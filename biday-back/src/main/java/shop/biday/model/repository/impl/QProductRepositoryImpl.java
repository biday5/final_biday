package shop.biday.model.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.*;
import shop.biday.model.repository.ImageRepository;
import shop.biday.model.repository.QProductRepository;
import shop.biday.service.impl.CategoryServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class QProductRepositoryImpl implements QProductRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public ProductModel findById(Long id) {
        return null;
    }

    @Override
    public Slice<ProductDto> findByFilter(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId) {
        return null;
    }
//
//    private final QProductEntity qProduct = QProductEntity.productEntity;
//    private final QBrandEntity qBrand = QBrandEntity.brandEntity;
//    private final QCategoryEntity qCategory = QCategoryEntity.categoryEntity;
//    private final QImageEntity qImage = QImageEntity.imageEntity;
//    private final QAuctionEntity qAuction = QAuctionEntity.auctionEntity;
//    private final ProductModel productModel;
//    private final CategoryServiceImpl categoryServiceImpl;
//    private final ImageRepository imageRepository;
//
//    private JPQLQuery<ProductDto> createBaseQuery(JPAQueryFactory queryFactory, Pageable pageable, Long categoryId, Long brandId, String keyword,
//                                                  String color, String order, Long lastItemId) {
//        JPQLQuery<ProductDto> query = queryFactory
//                .select(Projections.constructor(ProductDto.class,
//                        qProduct.id,
//                        qBrand.name.as("brand"),
//                        qCategory.name.as("category"),
//                        qProduct.name,
//                        qProduct.subName,
//                        qProduct.productCode,
//                        qProduct.price,
//                        qProduct.color,
//                        Projections.constructor(ImageModel.class,
//                                Expressions.constant("defaultImageId"),        // id
//                                Expressions.constant("defaultImageName"),      // name
//                                Expressions.constant("defaultImageExt"),       // ext
//                                Expressions.constant("defaultImageUrl"),       // url
//                                Expressions.constant("defaultImageType"),      // type
//                                Expressions.constant(0L),  // referenceId
//                                Expressions.constant(LocalDateTime.now())      // createdAt
//                        )
//                ))
//                .from(qProduct)
//                .leftJoin(qProduct.category, qCategory)
//                .leftJoin(qProduct.brand, qBrand);
//
//        if (categoryId != null) {
//            query.where(qCategory.id.eq(categoryId));
//        }
//
//        if (brandId != null) {
//            query.where(qProduct.brand.id.eq(brandId));
//        }
//
//        if (!keyword.isEmpty()) {
//            query.where(
//                    qProduct.name.containsIgnoreCase(keyword)
//                            .or(qProduct.subName.containsIgnoreCase(keyword))
//                            .or(qProduct.productCode.containsIgnoreCase(keyword))
//                            .or(qProduct.color.containsIgnoreCase(keyword))
//                            .or(qCategory.name.containsIgnoreCase(keyword))
//                            .or(qBrand.name.containsIgnoreCase(keyword))
//            );
//        }
//
//        if (!color.isEmpty()) {
//            query.where(qProduct.color.containsIgnoreCase(color));
//        }
//
//        // 입찰 수, 위시, endedAt
//        switch (order) {
//            case "가격 낮은 순":
//                if (lastItemId != null) {
//                    query.where(qProduct.price.gt(fetchPriceById(lastItemId)));
//                } else {
//                    query.orderBy(qProduct.price.asc());
//                }
//                break;
//            case "가격 높은 순":
//                if (lastItemId != null) {
//                    query.where(qProduct.price.lt(fetchPriceById(lastItemId)));
//                } else {
//                    query.orderBy(qProduct.price.desc());
//                }
//                break;
//            case "오래된 순":
//                if (lastItemId != null) {
//                    query.where(qProduct.createdAt.gt(fetchCreatedAtById(lastItemId)));
//                } else {
//                    query.orderBy(qProduct.createdAt.asc());
//                }
//                break;
//            case "최신순":
//                if (lastItemId != null) {
//                    query.where(qProduct.createdAt.lt(fetchCreatedAtById(lastItemId)));
//                } else {
//                    query.orderBy(qProduct.createdAt.desc());
//                }
//                break;
//            case "경매 적은 순":
//                if (lastItemId != null) {
//                    query.where(qProduct.auctions.any().count().gt(fetchAuctionById(lastItemId)));
//                } else {
//                    query.orderBy(qProduct.auctions.any().count().asc());
//                }
//                break;
//            case "경매 많은 순":
//                if (lastItemId != null) {
//                    query.where(qProduct.auctions.any().count().lt(fetchAuctionById(lastItemId)));
//                } else {
//                    query.orderBy(qProduct.auctions.any().count().desc());
//                }
//                break;
////            case "위시 많은 순":
////                query.orderBy(qProduct.createdAt.desc());
////                if (lastItemId != null) {
////                    query.where(qProduct.createdAt.lt(fetchCreatedAtById(lastItemId)));
////                }
////                break;
//            default:
//                if (lastItemId != null) {
//                    query.where(qProduct.id.gt(lastItemId));
//                } else {
//                    query.orderBy(qProduct.id.asc());
//                }
//                break;
//        }
//
//        return query.limit(pageable.getPageSize() + 1); // 페이지 사이즈 + 1을 반환하여 더 많은 데이터가 있는지 확인
//    }
//
//    private Long fetchPriceById(Long id) {
//        return queryFactory
//                .select(qProduct.price)
//                .from(qProduct)
//                .where(qProduct.id.eq(id))
//                .fetchOne();
//    }
//
//    private LocalDateTime fetchCreatedAtById(Long id) {
//        return queryFactory
//                .select(qProduct.createdAt)
//                .from(qProduct)
//                .where(qProduct.id.eq(id))
//                .fetchOne();
//    }
//
//    private Long fetchAuctionById(Long id) {
//        return queryFactory
//                .select(qProduct.auctions.any().count())
//                .from(qProduct)
//                .where(qProduct.id.eq(id))
//                .fetchOne();
//    }
//
//    @Override
//    public Slice<ProductDto> findByFilter(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemValue) {
//        System.out.println("Pageable: " + pageable + " categoryId: " + categoryId + " brandId: " + brandId + " keyword: " + keyword + " color: " + color + " order: " + order + " lastItemValue: " + lastItemValue);
//
//        List<ProductDto> list = createBaseQuery(queryFactory, pageable, categoryId, brandId, keyword, color, order, lastItemValue)
//                .fetch();
//
//        boolean hasNext = list.size() > pageable.getPageSize(); // 페이지 사이즈보다 많으면 다음 페이지가 있음
//        if (hasNext) {
//            list = list.subList(0, pageable.getPageSize()); // 페이지 사이즈까지만 유지
//        }
//
//        if (!list.isEmpty()) {
//            for(ProductDto productDto: list) {
//                ImageModel imageModel = imageRepository.findByNameAndType(productDto.getProductCode(), "상품");
//                if (imageModel != null) {
//                    productDto.setImage(imageModel);
//                }
//            }
//        }
//
//        return new SliceImpl<>(list, pageable, hasNext);
//    }
//
//    // 하기!!!
//    @Override
//    public ProductModel findById(Long id) {
//        // 주어진 제품 ID에 대한 경매 목록을 먼저 가져옵니다.
//        List<AuctionDto> auctions = queryFactory
//                .select(Projections.constructor(AuctionDto.class,
//                        qAuction.id,
//                        qAuction.userId,
//                        qProduct.name.as("product"),
//                        qAuction.startingBid,
//                        qAuction.startedAt,
//                        qAuction.endedAt,
//                        qAuction.status,
//                        qAuction.createdAt,
//                        qAuction.updatedAt
//                ))
//                .from(qAuction)
//                .leftJoin(qAuction.product, qProduct)
//                .where(qAuction.product.id.eq(id))
//                .orderBy(qAuction.endedAt.asc())
//                .fetch();
//
//        // 제품 세부 사항과 해당 제품의 경매 목록을 가져옵니다.
//        ProductModel product = queryFactory
//                .select(Projections.constructor(ProductModel.class,
//                        qProduct.id,
//                        qBrand.name.as("brand"),
//                        qCategory.name.as("category"),
//                        qProduct.name,
//                        qProduct.subName,
//                        qProduct.productCode,
//                        qProduct.price,
//                        qProduct.color,
//                        qProduct.description,
//                        qProduct.createdAt,
//                        qProduct.updatedAt,
//                        Projections.constructor(ImageModel.class,
//                                Expressions.constant("defaultImageId"),        // id
//                                Expressions.constant("defaultImageName"),      // name
//                                Expressions.constant("defaultImageExt"),       // ext
//                                Expressions.constant("defaultImageUrl"),       // url
//                                Expressions.constant("defaultImageType"),      // type
//                                Expressions.constant(0L),  // referenceId
//                                Expressions.constant(LocalDateTime.now())      // createdAt
//                        ),
//                        Projections.list(Projections.constructor(AuctionDto.class,
//                                qAuction.id,
//                                qAuction.userId,
//                                qProduct.name.as("product"),
//                                qAuction.startingBid,
//                                qAuction.startedAt,
//                                qAuction.endedAt,
//                                qAuction.status,
//                                qAuction.createdAt,
//                                qAuction.updatedAt))
//                ))
//                .from(qProduct)
//                .leftJoin(qProduct.category, qCategory)
//                .leftJoin(qProduct.brand, qBrand)
//                .leftJoin(qProduct.auctions, qAuction)
//                .where(qProduct.id.eq(id))
//                .fetchFirst();
//
//        // 필요에 따라 가져온 경매를 productDto에 설정합니다.
//        if (product != null) {
//            product.setAuctions(auctions); // 경매 리스트를 설정
//
//            ImageModel imageModel = imageRepository.findByNameAndType(product.getProductCode(), "상품");
//            if(imageModel != null) {
//                product.setImage(imageModel);
//            } else {
//                product.setImage(imageRepository.findByType("에러"));
//            }
//        }
//
//        return product;
//    }
}