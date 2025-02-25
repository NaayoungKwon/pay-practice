package practice.account.application.out;

import java.math.BigDecimal;
import practice.account.domain.ExternalAccount;
import practice.account.domain.TransactionHistory;

public interface SaveTransactionPort {

  TransactionHistory saveExternalDeposit(ExternalAccount externalAccount, Long accountId, BigDecimal amount);
  void rollbackExternalWithdraw(Long accountId, BigDecimal amount);
}
