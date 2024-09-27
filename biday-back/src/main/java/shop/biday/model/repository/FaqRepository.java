package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.FaqEntity;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, Long> {
}
