package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.MessengerEntity;

@Repository
public interface MessengerRepository extends JpaRepository<MessengerEntity, Long> {
}
