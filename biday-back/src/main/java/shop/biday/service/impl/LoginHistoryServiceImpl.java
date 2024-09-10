package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.LoginHistoryModel;
import shop.biday.model.entity.LoginHistoryEntity;
import shop.biday.model.repository.LoginHistoryRepository;
import shop.biday.service.LoginHistoryService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    public List<LoginHistoryEntity> findAll() {
        return loginHistoryRepository.findAll();
    };

    @Override
    public Optional<LoginHistoryEntity> findById(Long id) {
        return loginHistoryRepository.findById(id);
    };

    @Override
    public LoginHistoryEntity save(LoginHistoryModel loginHistoryModel) {
        return null;
    };

    @Override
    public boolean existsById(Long id) {
        return loginHistoryRepository.existsById(id);
    };

    @Override
    public long count() {
        return loginHistoryRepository.count();
    };

    @Override
    public void deleteById(Long id) {
        loginHistoryRepository.deleteById(id);
    };
}
