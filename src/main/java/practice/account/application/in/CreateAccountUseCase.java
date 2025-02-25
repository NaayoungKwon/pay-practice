package practice.account.application.in;

import practice.account.domain.Account;
import practice.account.domain.ExternalAccount;

public interface CreateAccountUseCase {

  Account createMainAccount(Long userId);
  Account createSavingAccount(Long userId);
  ExternalAccount registerExternalAccount(Long userId, String bankCode, String accountNumber);

}
