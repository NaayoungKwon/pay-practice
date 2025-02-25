package practice.account.application.in;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.account.application.out.ExternalBankPort;
import practice.account.application.out.LoadAccountPort;
import practice.account.application.out.RegisterAccountPort;
import practice.account.domain.Account;
import practice.account.domain.AccountType;
import practice.account.domain.ExternalAccount;
import practice.account.domain.TransactionHistory;
import practice.common.exception.ExternalAccountLimitExceededException;
import practice.common.lock.DistributedLock;

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
  public ExternalAccount registerExternalAccount(Long userId, String bankCode,
      String accountNumber) {
    return registerAccountPort.registerExternalAccount(userId, bankCode, accountNumber);
  }

  @Override
  @DistributedLock(key="ACCOUNT", value = "#userId")
  public void depositToMainAccount(Long userId, BigDecimal amount) {
    Account account = loadAccountPort.findAccount(userId, AccountType.MAIN);
    ExternalAccount externalAccount = loadAccountPort.findExternalAccount(userId);
    TransactionHistory transactionHistory = externalBankPort.withdraw(externalAccount,account.getAccountId(), amount);
    account.deposit(transactionHistory);
    updateAccountUseCase.updateAccount(account);
  }

  @Override
  @DistributedLock(key="ACCOUNT", value = "#userId")
  public void depositToSavingAccount(Long userId, BigDecimal amount) {
    Account mainAccount = loadAccountPort.findMainAccountWithTodayWithdraw(userId);
    if(!mainAccount.canWithdrawNow(amount) && !mainAccount.canWithdrawToExternalAccount()) {
      throw new ExternalAccountLimitExceededException();
    } else if (!mainAccount.canWithdrawNow(amount)) {
      ExternalAccount externalAccount = loadAccountPort.findExternalAccount(userId);
      TransactionHistory transactionHistory = externalBankPort.withdraw(externalAccount, mainAccount.getAccountId(), mainAccount.calculateRequiredAmount(amount) );
      mainAccount.deposit(transactionHistory);
    }

    Account savingAccount = loadAccountPort.findAccount(userId, AccountType.SAVINGS);
    mainAccount.withdraw(amount, savingAccount);
    savingAccount.deposit(amount, mainAccount );
    updateAccountUseCase.updateAccount(List.of(mainAccount, savingAccount));
  }
}
