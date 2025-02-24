package practice.adaptor.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import practice.account.application.out.SaveTransactionPort;
import practice.account.domain.ExternalAccount;
import practice.account.domain.TransactionHistory;
import practice.account.domain.TransactionType;
import practice.adaptor.out.entity.AccountEntity;
import practice.adaptor.out.entity.TransactionEntity;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdaptor implements SaveTransactionPort {

  private final TransactionJpaRepository transactionJpaRepository;
  private final AccountMapper accountMapper;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public TransactionHistory saveExternalDeposit(ExternalAccount externalAccount, Long accountId, BigDecimal amount) {
    TransactionEntity transaction = transactionJpaRepository.save(
        getTransactionEntity(externalAccount,accountId,  amount));
    return accountMapper.toDomain(transaction);
  }

  private TransactionEntity getTransactionEntity(ExternalAccount externalAccount, Long accountId, BigDecimal amount) {
    return TransactionEntity.builder()
        .createdDate(LocalDate.now())
        .account(new AccountEntity(accountId))
        .transactionType(TransactionType.DEPOSIT_PENDING.name())
        .amount(amount)
        .counterPartyBank(externalAccount.getBankName())
        .counterPartyAccountId(externalAccount.getAccountNumber())
        .build();
  }
}
