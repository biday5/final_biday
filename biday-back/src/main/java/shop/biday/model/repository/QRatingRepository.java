package shop.biday.model.repository;

import shop.biday.model.entity.RatingEntity;

import java.util.List;

public interface QRatingRepository {
    List<RatingEntity> findBySeller(Long userId);
}
