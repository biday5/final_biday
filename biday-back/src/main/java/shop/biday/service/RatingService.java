package shop.biday.service;

import shop.biday.model.domain.RatingModel;
import shop.biday.model.entity.RatingEntity;

import java.util.List;

public interface RatingService {
    List<RatingEntity> findBySeller(String token, Long userId);
    RatingEntity save(String token, RatingModel ratingModel);
//    double findRate(Long userId);
}
