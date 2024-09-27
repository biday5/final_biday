package shop.biday.service;

import shop.biday.model.domain.SizeModel;
import shop.biday.model.entity.SizeEntity;

import java.util.List;
import java.util.Optional;

public interface SizeService {
    List<SizeEntity> findAll();
    Optional<SizeEntity> findById(Long id);
    List<SizeEntity> findAllByProductId(Long productId);
    SizeEntity findBySize(String size);
    SizeEntity save(String token, SizeModel size);
    SizeEntity update(String token, SizeModel size);
    String deleteById(String token, Long id);
}
