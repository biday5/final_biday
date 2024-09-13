package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionService {
    AuctionModel findById(Long id);
    Slice<AuctionDto> findByUser(Long userId, String period, LocalDateTime cursor, Pageable pageable);
    boolean existsById(Long id);
    AuctionEntity save(AuctionModel auction);
    AuctionEntity update(AuctionModel auction);
    void deleteById(Long id);
}
