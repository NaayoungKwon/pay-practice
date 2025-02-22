package practice.account.application.out;

import java.math.BigDecimal;
import practice.account.domain.ExternalAccount;

public interface ExternalBankPort {

  BigDecimal getBalance(ExternalAccount externalAccount, BigDecimal amount);
}
