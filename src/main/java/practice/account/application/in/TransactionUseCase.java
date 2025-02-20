package practice.account.application.in;

import java.math.BigDecimal;

public interface TransactionUseCase {

  void depositToMainAccount(Long userId, BigDecimal amount);
  void depositToSavingAccount(Long userId, BigDecimal amount);

}
