package shop.biday.model.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.*;
import shop.biday.model.repository.QAuctionRepository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class QAuctionRepositoryImpl implements QAuctionRepository {
    private final JPAQueryFactory queryFactory;

    private final QAuctionEntity qAuction = QAuctionEntity.auctionEntity;
    private final QProductEntity qProduct = QProductEntity.productEntity;
    private final QBrandEntity qBrand = QBrandEntity.brandEntity;
    private final QCategoryEntity qCategory = QCategoryEntity.categoryEntity;
    private final QAwardEntity qAward = QAwardEntity.awardEntity;

    @Override
    public AuctionModel findById(Long id) {
        return null;
    }

    @Override
    public Slice<AuctionDto> findByUser(Long userId, String period, LocalDateTime cursor, Pageable pageable) {
        return null;
    }
}
