package shop.biday.service;

import shop.biday.model.domain.RatingModel;
import shop.biday.model.domain.UserModel;
import shop.biday.model.entity.RatingEntity;
import shop.biday.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    List<RatingEntity> findAll();
    Optional<RatingEntity> findById(Long id);
    RatingEntity save(RatingModel ratingModel);
    boolean existsById(Long id);
    long count();
    void deleteById(Long id);
}
