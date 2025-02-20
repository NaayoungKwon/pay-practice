package practice.adaptor.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import practice.account.application.in.UpdateAccountPort;
import practice.account.application.out.LoadAccountPort;
import practice.account.application.out.RegisterAccountPort;
import practice.account.domain.Account;
import practice.account.domain.AccountType;
import practice.account.domain.TransactionType;
import practice.adaptor.out.entity.AccountEntity;
import practice.adaptor.out.entity.TransactionEntity;
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
        .balance(BigDecimal.ZERO)
        .build();
    return accountMapper.toDomain(accountJpaRepository.save(accountEntity));
  }

  @Override
  public Account findAccount(Long userId, AccountType accountType) {
    return accountMapper.toDomain(accountJpaRepository.findByUserAndType(new UserEntity(userId), accountType.name()));
  }

  @Override
  public Account findMainAccountWithTodayWithdraw(Long userId) {
    LocalDate today = LocalDate.now();
    AccountEntity accountEntity = accountJpaRepository.findByUserAndTypeFetch(
        userId, AccountType.MAIN.name());
    BigDecimal todayWithdrawal = accountEntity.getTransactions().stream()
        .filter(t->t.getCreatedDate().isEqual(today))
        .filter(t->t.getTransactionType().equals(TransactionType.WITHDRAW.name()))
        .map(TransactionEntity::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return accountMapper.toDomain(accountEntity, todayWithdrawal);
  }

  @Override
  public void updateAccount(Account account) {
    accountJpaRepository.save(accountMapper.toEntity(account));
  }

  @Override
  public void updateAccount(List<Account> accounts) {
    List<AccountEntity> accountEntities = accounts.stream().map(accountMapper::toEntity).toList();
    accountJpaRepository.saveAll(accountEntities);
  }
}
