package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.RatingModel;
import shop.biday.model.entity.RatingEntity;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long>, QRatingRepository {
    boolean existsById(Long id);
    RatingEntity save(RatingModel rating);
}
