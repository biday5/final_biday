package shop.biday.service;

import org.springframework.stereotype.Service;
import shop.biday.model.domain.AccountModel;
import shop.biday.model.entity.AccountEntity;

import java.util.List;
import java.util.Optional;

@Service
public interface AccountService {
    List<AccountEntity> findAll();
    Optional<AccountEntity> findById(Long id);
    AccountEntity save(AccountModel accountModel);
    boolean existsById(Long id);
    long count();
    void deleteById(Long id);
}
