package shop.biday.service;

import org.springframework.stereotype.Service;
import shop.biday.model.domain.UserModel;
import shop.biday.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long id);
    UserEntity save(UserModel userModel);
    boolean existsById(Long id);
    long count();
    void deleteById(Long id);
}
