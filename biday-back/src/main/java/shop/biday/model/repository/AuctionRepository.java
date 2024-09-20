package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.entity.AuctionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<AuctionEntity, Long>, QAuctionRepository {
    boolean existsById(Long id);
    AuctionEntity save(AuctionModel auction);
    void deleteById(Long id);
}
