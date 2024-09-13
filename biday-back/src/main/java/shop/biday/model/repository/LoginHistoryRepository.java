package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.entity.LoginHistoryEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long> {
    Optional<LoginHistoryEntity> findFirstByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
