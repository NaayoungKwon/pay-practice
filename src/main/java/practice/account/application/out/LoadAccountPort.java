package practice.account.application.out;

import practice.account.domain.Account;
import practice.account.domain.AccountType;
import practice.account.domain.ExternalAccount;

public interface LoadAccountPort {

  Account findAccount(Long userId, AccountType accountType);
  Account findMainAccountWithTodayWithdraw(Long userId);
  ExternalAccount findExternalAccount(Long userId);
}
