package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;

import java.time.LocalDateTime;

public interface AuctionService {
    AuctionModel findById(Long id);
    Slice<AuctionDto> findByTime(String order, Long cursor, Pageable pageable);
    Slice<AuctionDto> findByUser(String token, Long userId, String period, Long cursor, Pageable pageable);
    boolean existsById(Long id);
    AuctionEntity save(String token, AuctionModel auction);
    AuctionEntity update(String token, AuctionModel auction);
    void deleteById(String token, Long id);
}
