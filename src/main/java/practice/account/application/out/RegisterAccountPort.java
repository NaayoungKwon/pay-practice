package practice.account.application.out;

import practice.account.domain.Account;
import practice.account.domain.AccountType;

public interface RegisterAccountPort {

  Account createAccount(Long userId, AccountType accountType);

}
