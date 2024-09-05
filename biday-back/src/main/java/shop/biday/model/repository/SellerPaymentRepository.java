package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.SellerPaymentEntity;

@Repository
public interface SellerPaymentRepository extends JpaRepository<SellerPaymentEntity, Long> {
}
