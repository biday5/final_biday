package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.NotificationTypeEntity;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationTypeEntity, Long> {
}
