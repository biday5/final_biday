package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.UserModel;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.repository.UserRepository;
import shop.biday.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    };

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    };

    @Override
    public UserEntity save(UserModel userModel) {
        return userRepository.save(UserEntity.builder()
                .name(userModel.getName())
                .email(userModel.getEmail())
                .password(userModel.getPassword())
                .phone(userModel.getPhone())
                .build());
    };

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    };

    @Override
    public long count() {
        return userRepository.count();
    };

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    };
}
