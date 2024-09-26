package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.domain.UserModel;
import shop.biday.model.dto.UserDto;
import shop.biday.model.entity.AddressEntity;
import shop.biday.model.entity.enums.AddressType;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.repository.AddressRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.service.ImageService;
import shop.biday.service.RatingService;
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
    private final ImageService imageService;
    private final RatingService ratingService;

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDto findByUserId(String id) {
        log.info("Find User: {}", id);
        UserEntity user = userRepository.findByEmail(id);

        return Optional.ofNullable(user)
                .map(u -> {
                    log.info("Find User Image: {}", u.getId());
                    int rate = (int) ratingService.findSellerRate(String.valueOf(u.getId()));
                    ImageModel userImage = (rate == 0)
                            ? imageService.findByOriginalNameAndType(String.valueOf(rate), "평점")
                            : imageService.findByOriginalNameAndType("2", "평점");

                    UserDto userDto = new UserDto(u.getEmail(), u.getName(), userImage);
                    log.debug("Find User success: {}", userDto);
                    return userDto;
                })
                .orElseGet(() -> {
                    log.error("Find User failed");
                    return null;
                });
    }

    @Override
    public UserEntity save(UserModel userModel) {
        log.info("userModel:{}", userModel);
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

    public boolean checkEmail(UserModel userModel) {
        return userRepository.existsByEmail(userModel.getEmail());
    }

    public Boolean checkPhone(UserModel userModel) {
        return userRepository.existsByPhone(userModel.getPhoneNum());
    }

    public Boolean existsByPasswordAndEmail(UserModel userModel) {
        UserEntity user = userRepository.findByEmail(userModel.getEmail());
        if (user != null) {
            return passwordEncoder.matches(userModel.getPassword(), user.getPassword());
        }
        return false;
    }

    public String getEmailByPhone(UserModel userModel) {
        return userRepository.findEmailByPhone(userModel.getPhoneNum())
                .orElseThrow(() -> new RuntimeException("User not found with phone: " + userModel.getPhoneNum()));
    }

    public String changePassword(UserModel userModel) {
        UserEntity user = userRepository.findByEmail(userModel.getEmail());
        if (user != null) {
            if (passwordEncoder.matches(userModel.getPassword(), user.getPassword())) {
                String encodedNewPassword = passwordEncoder.encode(userModel.getNewPassword());
                user.setPassword(encodedNewPassword);
                userRepository.save(user);
                return "비밀번호 변경이 완료 했습니다.";
            } else {
                return "예전 비밀번호가 틀렸습니다.";
            }
        } else {
            return "이메일 대상이 없습니다.";
        }
    }

}