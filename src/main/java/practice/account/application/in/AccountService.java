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
import practice.account.domain.ExternalAccount;

@Service
@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, TransactionUseCase {

  private final RegisterAccountPort registerAccountPort;
  private final LoadAccountPort loadAccountPort;
  private final UpdateAccountUseCase updateAccountUseCase;
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
    ExternalAccount externalAccount = loadAccountPort.findExternalAccount(userId);
    externalBankPort.getBalance(externalAccount, amount);
    account.deposit(amount,  externalAccount);
    updateAccountUseCase.updateAccount(account);
  }

  @Override
  @Transactional
  public void depositToSavingAccount(Long userId, BigDecimal amount) {
    Account mainAccount = loadAccountPort.findMainAccountWithTodayWithdraw(userId);
    if(!mainAccount.canWithdrawNow(amount) && !mainAccount.canWithdrawToExternalAccount()) {
      return ;
    } else if (!mainAccount.canWithdrawNow(amount)) {
      ExternalAccount externalAccount = loadAccountPort.findExternalAccount(userId);
      BigDecimal externalBankMoney = externalBankPort.getBalance(externalAccount, amount);
      mainAccount.deposit(externalBankMoney, externalAccount);
    }

    Account savingAccount = loadAccountPort.findAccount(userId, AccountType.SAVINGS);
    mainAccount.withdraw(amount, savingAccount);
    savingAccount.deposit(amount, mainAccount );
    updateAccountUseCase.updateAccount(List.of(mainAccount, savingAccount));
  }
}
