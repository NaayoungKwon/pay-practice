package practice.account.application.in;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.account.application.out.ExternalBankPort;
import practice.account.application.out.LoadAccountPort;
import practice.account.application.out.RegisterAccountPort;
import practice.account.domain.Account;
import practice.account.domain.AccountType;

@Service
@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, TransactionUseCase {

  private final RegisterAccountPort registerAccountPort;
  private final LoadAccountPort loadAccountPort;
  private final UpdateAccountPort updateAccountPort;
  private final ExternalBankPort externalBankPort;


  @Override
  public Account createMainAccount(Long userId) {
    return registerAccountPort.createAccount(userId, AccountType.MAIN);
  }

  @Override
  public Account createSavingAccount(Long userId) {
    return registerAccountPort.createAccount(userId, AccountType.SAVINGS);
  }

  @Override
  @Transactional
  public void depositToMainAccount(Long userId, BigDecimal amount) {
    Account account = loadAccountPort.findAccount(userId, AccountType.MAIN);
    account.deposit(amount);
    updateAccountPort.updateAccount(account);
  }

  @Override
  @Transactional
  public void depositToSavingAccount(Long userId, BigDecimal amount) {
    Account mainAccount = loadAccountPort.findMainAccountWithTodayTransaction(userId);
    if(!(mainAccount.canWithdraw(amount) || mainAccount.canWithdrawToExternalAccount())) {
      return;
    } else if (!mainAccount.canWithdraw(amount)) {
      BigDecimal externalBankMoney = externalBankPort.getBalance(userId, amount); // 다른건 롤백한다 쳐도 얘는 우째
      mainAccount.deposit(externalBankMoney);
    }

    Account savingAccount = loadAccountPort.findAccount(userId, AccountType.SAVINGS);
    mainAccount.withdraw(amount);
    savingAccount.deposit(amount);
    updateAccountPort.updateAccount(List.of(mainAccount, savingAccount));
  }
}
