package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.RefundEntity;

@Repository
public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
}
