package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.AuctionEntity;

@Repository
public interface AuctionRepository extends JpaRepository<AuctionEntity, Long>, QAuctionRepository {
    boolean existsById(Long id);

    AuctionEntity save(AuctionEntity auction);

    void deleteById(Long id);
}
