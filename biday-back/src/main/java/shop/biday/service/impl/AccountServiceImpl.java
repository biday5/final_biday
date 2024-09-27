package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AccountModel;
import shop.biday.model.entity.AccountEntity;
import shop.biday.model.repository.AccountRepository;
import shop.biday.service.AccountService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public List<AccountEntity> findAll() {
        return accountRepository.findAll();
    }

    ;

    @Override
    public Optional<AccountEntity> findById(Long id) {
        return accountRepository.findById(id);
    }

    ;

    @Override
    public AccountEntity save(AccountModel accountModel) {
        return null;
    }

    ;

    @Override
    public boolean existsById(Long id) {
        return accountRepository.existsById(id);
    }

    ;

    @Override
    public long count() {
        return accountRepository.count();
    }

    ;

    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    ;
}
