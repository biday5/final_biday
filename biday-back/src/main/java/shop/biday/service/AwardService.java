package shop.biday.service;

import shop.biday.model.entity.AwardEntity;

import java.util.List;

public interface AwardService {

    List<AwardEntity> findAll();

    AwardEntity findById(Long id);

    AwardEntity save(AwardEntity award);
}
