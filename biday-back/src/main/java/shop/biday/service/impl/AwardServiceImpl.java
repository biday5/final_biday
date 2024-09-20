package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.dto.AwardDto;
import shop.biday.model.repository.AwardRepository;
import shop.biday.model.repository.QAwardRepository;
import shop.biday.service.AwardService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {
    private final AwardRepository repository;
    private final QAwardRepository bidRepository;

    @Override
    public AwardModel findById(Long id) {
        try {
            return bidRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Slice<AwardDto> findByUser(Long userId, String period, LocalDateTime cursor, Pageable pageable) {
        try {
            return bidRepository.findByUserId(userId, period, cursor, pageable);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
