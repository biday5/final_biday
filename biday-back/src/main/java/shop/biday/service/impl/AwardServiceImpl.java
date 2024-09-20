package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.dto.AwardDto;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.entity.AwardEntity;
import shop.biday.model.repository.AwardRepository;
import shop.biday.service.AwardService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {

    private final AwardRepository awardRepository;

    @Override
    public List<AwardEntity> findAll() {
        return awardRepository.findAll();
    }

    @Override
    public AwardEntity findById(Long id) {
        return awardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 데이터입니다."));
    }

    @Override
    public AwardEntity save(AwardEntity award) {
        return awardRepository.save(award);
    }

    @Override
    public AwardModel findByAwardId(Long id) {
        return null;
//        try {
//            return awardRepository.findByAwardId(id);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return null;
//        }
    }

    @Override
    public Slice<AwardDto> findByUser(Long userId, String period, LocalDateTime cursor, Pageable pageable) {
        try {
            return awardRepository.findByUserId(userId, period, cursor, pageable);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
