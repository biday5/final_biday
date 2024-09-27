package shop.biday.service;

import shop.biday.model.domain.ShipperModel;
import shop.biday.model.entity.ShipperEntity;

import java.util.List;
import java.util.Optional;

public interface ShipperService {
    List<ShipperEntity> findAll();
    Optional<ShipperEntity> findById(Long id);
    ShipperEntity save(String token, ShipperModel brand);
    ShipperEntity update(String token, ShipperModel brand);
    String deleteById(String token, Long id);
}
