package shop.biday.service.impl;

import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AddressModel;
import shop.biday.model.entity.AddressEntity;
import shop.biday.model.repository.AddressRepository;
import shop.biday.service.AddressService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public List<AddressEntity> findAll() {
        return addressRepository.findAll();
    };
    @Override
    public Optional<AddressEntity> findById(Long id){
        return addressRepository.findById(id);
    };
    @Override
    public AddressEntity save(AddressModel addressModel) {
        return null;
    };
    @Override
    public boolean existsById(Long id) {
        return addressRepository.existsById(id);
    };
    @Override
    public long count() {
        return addressRepository.count();
    };
    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    };
}
