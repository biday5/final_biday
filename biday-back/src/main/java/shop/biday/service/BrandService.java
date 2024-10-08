package shop.biday.service;

import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<BrandEntity> findAll();

    Optional<BrandEntity> findById(Long id);

    BrandEntity save(String token, BrandModel brand);

    BrandEntity update(String token, BrandModel brand);

    String deleteById(String token, Long id);
}
