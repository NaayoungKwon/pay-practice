package practice.account.application.in;

import java.util.List;
import practice.account.domain.Account;

public interface UpdateAccountUseCase {


  void updateAccount(Account account);
  void updateAccount(List<Account> accounts);
}
