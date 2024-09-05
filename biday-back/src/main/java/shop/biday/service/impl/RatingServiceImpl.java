package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.RatingModel;
import shop.biday.model.entity.RatingEntity;
import shop.biday.model.repository.RatingRepository;
import shop.biday.service.RatingService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;

    @Override
    public List<RatingEntity> findAll() {
        return ratingRepository.findAll();
    };

    @Override
    public Optional<RatingEntity> findById(Long id) {
        return ratingRepository.findById(id);
    };

    @Override
    public RatingEntity save(RatingModel ratingModel) {
        return null;
    };

    @Override
    public boolean existsById(Long id) {
        return ratingRepository.existsById(id);
    };

    @Override
    public long count() {
        return ratingRepository.count();
    };

    @Override
    public void deleteById(Long id) {
        ratingRepository.deleteById(id);
    };
}
