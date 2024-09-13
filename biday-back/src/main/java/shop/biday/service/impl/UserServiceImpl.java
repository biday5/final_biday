package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.UserModel;
import shop.biday.model.entity.AddressEntity;
import shop.biday.model.entity.enums.AddressType;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.repository.AddressRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserEntity save(UserModel userModel) {
        log.info("userModel:{}",userModel);
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .name(userModel.getName())
                .email(userModel.getEmail())
                .password(passwordEncoder.encode(userModel.getPassword()))
                .phone(userModel.getPhoneNum())
                .role(UserEntity.Role.ROLE_USER)
                .status(true)
                .totalRating(2.0)
                .build());

        Long userId = savedUser.getId();


        AddressType addressType;
        try {
            addressType = AddressType.fromString(userModel.getType());
        } catch (IllegalArgumentException e) {
            addressType = AddressType.OTHER;
        }


        if (userModel.getStreetaddress() != null) {
            AddressEntity addressEntity = AddressEntity.builder()
                    .userId(userId)
                    .streetAddress(userModel.getStreetaddress())
                    .zipcode(userModel.getZipcode())
                    .detailAddress(userModel.getDetailaddress())
                    .pick(true)
                    .type(addressType)
                    .build();
            addressRepository.save(addressEntity);
        }

        return savedUser;
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean checkPhone(String phoneNum) {
        return userRepository.existsByPhone(phoneNum);
    }

    public Boolean checkPassword(String password) {
        return userRepository.existsByPassword(password);
    }
}