package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.biday.model.entity.LoginHistoryEntity;

public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long> {
}
