package practice.account.application.out;

import java.math.BigDecimal;
import practice.account.domain.Account;
import practice.account.domain.AccountType;

public interface ExternalBankPort {

  BigDecimal getBalance(Long userId, BigDecimal amount);
}
