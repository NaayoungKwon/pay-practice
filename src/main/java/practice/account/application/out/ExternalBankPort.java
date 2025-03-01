package practice.account.application.out;

import java.math.BigDecimal;
import practice.account.domain.ExternalAccount;
import practice.account.domain.TransactionHistory;

public interface ExternalBankPort {

  TransactionHistory withdraw(ExternalAccount externalAccount, Long accountId, BigDecimal amount);
}
