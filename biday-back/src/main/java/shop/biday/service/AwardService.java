package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.AwardModel;
import shop.biday.model.entity.AwardEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface AwardService {

    List<AwardEntity> findAll();

    AwardEntity findById(Long id);

    AwardEntity save(AwardEntity award);

    AwardModel findByAwardId(String token, Long id);

    Slice<AwardModel> findByUser(String token, String user, String period, LocalDateTime cursor, Pageable pageable);
}
