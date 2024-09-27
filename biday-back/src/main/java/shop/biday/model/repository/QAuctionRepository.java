package shop.biday.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;

public interface QAuctionRepository {
    AuctionModel findByAuctionId(Long id);

    Slice<AuctionDto> findByUser(String user, String period, Long cursor, Pageable pageable);

    Slice<AuctionDto> findByProduct(Long sizeId, String order, Long cursor, Pageable pageable);
}
