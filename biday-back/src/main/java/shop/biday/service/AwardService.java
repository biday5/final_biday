package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.dto.AwardDto;

import java.time.LocalDateTime;

public interface AwardService {
    AwardModel findById(Long id);
    Slice<AwardDto> findByUser(Long userId, String period, LocalDateTime cursor, Pageable pageable);
}
