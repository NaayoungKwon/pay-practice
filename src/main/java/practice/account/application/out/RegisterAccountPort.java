package practice.account.application.out;

import practice.account.domain.Account;
import practice.account.domain.AccountType;
import practice.account.domain.ExternalAccount;

public interface RegisterAccountPort {

  Account createAccount(Long userId, AccountType accountType);
  ExternalAccount registerExternalAccount(Long userId, String bankCode, String accountNumber);
}
