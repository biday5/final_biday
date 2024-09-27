package shop.biday.service;

import shop.biday.model.domain.AddressModel;
import shop.biday.model.entity.AddressEntity;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    List<AddressEntity> findAll();

    Optional<AddressEntity> findById(Long id);

    AddressEntity save(AddressModel addressModel);

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);
}
