package shop.biday.service;

import shop.biday.model.domain.RatingModel;
import shop.biday.model.entity.RatingEntity;

import java.util.List;

public interface RatingService {
    double findSellerRate(String sellerId);

    List<RatingEntity> findBySeller(String token, String sellerId);

    RatingEntity save(String token, RatingModel ratingModel);
}
