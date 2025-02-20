package practice.account.application.in;

import practice.account.domain.Account;

public interface CreateAccountUseCase {

  Account createMainAccount(Long userId);
  Account createSavingAccount(Long userId);

}
