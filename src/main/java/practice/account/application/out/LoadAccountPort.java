package practice.account.application.out;

import practice.account.domain.Account;
import practice.account.domain.AccountType;

public interface LoadAccountPort {

  Account findAccount(Long userId, AccountType accountType);
  Account findMainAccountWithTodayTransaction(Long userId);
}
