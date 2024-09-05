package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.ShipperEntity;

@Repository
public interface ShipperRepository extends JpaRepository<ShipperEntity, Long> {
}
