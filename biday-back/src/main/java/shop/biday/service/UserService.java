package shop.biday.service;

import shop.biday.model.domain.UserModel;
import shop.biday.model.dto.UserDto;
import shop.biday.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);

    UserDto findByUserId(String id);

    UserEntity save(UserModel userModel);

    boolean existsById(Long id);

    long count();

    void deleteById(Long id);
}
