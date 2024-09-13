package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.entity.AuctionEntity;
import shop.biday.model.entity.BidEntity;

@Repository
public interface BidRepository extends JpaRepository<BidEntity, Long> {

}
