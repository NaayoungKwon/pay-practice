package practice.adaptor.out;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import practice.account.application.in.UpdateAccountPort;
import practice.account.application.out.LoadAccountPort;
import practice.account.application.out.RegisterAccountPort;
import practice.account.domain.Account;
import practice.account.domain.AccountType;
import practice.adaptor.out.entity.AccountEntity;
import practice.adaptor.out.entity.UserEntity;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, RegisterAccountPort,
    UpdateAccountPort {

  private final AccountMapper accountMapper;
  private final AccountJpaRepository accountJpaRepository;

  @Override
  public Account createAccount(Long userId, AccountType accountType) {
    AccountEntity accountEntity = AccountEntity.builder()
        .type(accountType.name())
        .user(UserEntity.builder().id(userId).build())
        .build();
    return accountMapper.toDomain(accountJpaRepository.save(accountEntity));
  }

  @Override
  public Account findAccount(Long userId, AccountType accountType) {
    return null;
  }

  @Override
  public Account findMainAccountWithTodayTransaction(Long userId) {
    return null;
  }

  @Override
  public void updateAccount(Account account) {

  }

  @Override
  public void updateAccount(List<Account> accounts) {

  }
}
