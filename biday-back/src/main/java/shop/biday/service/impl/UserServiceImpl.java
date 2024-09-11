package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.UserModel;
import shop.biday.model.entity.UserEntity;
import shop.biday.model.repository.UserRepository;
import shop.biday.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        System.out.println("회원가입");

        String password = bCryptPasswordEncoder.encode(userModel.getPassword());

        return userRepository.save(UserEntity.builder()
                .name(userModel.getName())
                .email(userModel.getEmail())
                .password(password)
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
