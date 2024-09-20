package shop.biday.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AddressModel;
import shop.biday.model.entity.AddressEntity;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.entity.enums.AddressType;
import shop.biday.model.repository.AddressRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.AddressService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

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

        AddressType addressType;
        try {
            addressType = AddressType.fromString(addressModel.getType());
        } catch (IllegalArgumentException e) {
            addressType = AddressType.OTHER;
        }

        UserEntity user = userRepository.findByEmail(addressModel.getEmail());

        if (countByEmail(addressModel.getEmail())  >= 3) {
            throw new IllegalStateException("최대 주소 가능 갯수는 3개 입니다.");
        }

        AddressEntity existingAddress = addressRepository.findByUserId(user.getId());

        AddressEntity addressEntity;

        if (existingAddress == null) {
            addressEntity = AddressEntity.builder()
                    .userId(user.getId())
                    .streetAddress(addressModel.getStreetAddress())
                    .detailAddress(addressModel.getDetailAddress())
                    .zipcode(addressModel.getZipcode())
                    .type(addressType)
                    .pick(true)
                    .build();
        } else {
            addressEntity = AddressEntity.builder()
                    .userId(user.getId())
                    .streetAddress(addressModel.getStreetAddress())
                    .detailAddress(addressModel.getDetailAddress())
                    .zipcode(addressModel.getZipcode())
                    .type(addressType)
                    .pick(false)
                    .build();
        }

        return addressRepository.save(addressEntity);
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

    public long countByEmail(String token){
        String email = jwtUtil.getEmail(token);
        UserEntity user = userRepository.findByEmail(email);
        return addressRepository.countByUserId(user.getId());
    }

    public String pick(Long id) {
        Optional<AddressEntity> addressEntityOptional = addressRepository.findById(id);

        if (addressEntityOptional.isPresent()) {
            AddressEntity selectedAddress = addressEntityOptional.get();
            Long userId = selectedAddress.getUserId();

            List<AddressEntity> addresses = addressRepository.findByUserIdAndPick(userId, true);
            for (AddressEntity address : addresses) {
                address.setPick(false);
                addressRepository.save(address);
            }

            selectedAddress.setPick(true);
            addressRepository.save(selectedAddress);

            return "주소 업데이트가 성공했습니다.";
        } else {
            throw new EntityNotFoundException("주소 번호가 업습니다.");
        }
    }

    public List<AddressEntity> findAllByEmail(String token) {
        String email = jwtUtil.getEmail(token);
        UserEntity user = userRepository.findByEmail(email);
        return addressRepository.findAllByUserId(user.getId());
    }
}
