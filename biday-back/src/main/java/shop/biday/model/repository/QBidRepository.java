package shop.biday.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.BidModel;
import shop.biday.model.dto.BidDto;

import java.time.LocalDateTime;

public interface QBidRepository {
    BidModel findById(Long id);
    Slice<BidDto> findByUserId(Long userId, String period, LocalDateTime cursor, Pageable pageable);
}
