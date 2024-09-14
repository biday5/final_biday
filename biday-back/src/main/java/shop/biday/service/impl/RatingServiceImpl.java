package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.RatingModel;
import shop.biday.model.entity.RatingEntity;
import shop.biday.model.repository.QRatingRepository;
import shop.biday.model.repository.RatingRepository;
import shop.biday.service.RatingService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository repository;
    private final QRatingRepository ratingRepository;

    @Override
    public List<RatingEntity> findBySeller(Long userId) {
        try {
            return ratingRepository.findBySeller(userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public RatingEntity save(RatingModel ratingModel) {
        try {
            return repository.save(ratingModel);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
