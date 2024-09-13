package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.LoginHistoryModel;
import shop.biday.model.entity.LoginHistoryEntity;
import shop.biday.model.repository.LoginHistoryRepository;
import shop.biday.service.LoginHistoryService;

import java.time.LocalDateTime;
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
    }

    @Override
    public LoginHistoryEntity save(LoginHistoryModel loginHistoryModel) {
        Long userId = loginHistoryModel.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("유저ID 가 없습니다.");
        }

        LoginHistoryEntity loginHistoryEntity = LoginHistoryEntity.builder()
                .userId(userId)
                .build();

        return loginHistoryRepository.save(loginHistoryEntity);

    };

    @Override
    public Optional<LoginHistoryEntity> findByUserId(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        return loginHistoryRepository.findFirstByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay);

    }

    ;
}
