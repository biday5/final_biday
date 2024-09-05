package shop.biday.service;

import org.springframework.stereotype.Service;
import shop.biday.model.domain.LoginHistoryModel;
import shop.biday.model.entity.LoginHistoryEntity;

import java.util.List;
import java.util.Optional;

@Service
public interface LoginHistoryService {
    List<LoginHistoryEntity> findAll();
    Optional<LoginHistoryEntity> findById(Long id);
    LoginHistoryEntity save(LoginHistoryModel loginHistoryModel);
    boolean existsById(Long id);
    long count();
    void deleteById(Long id);
}
