package practice.account.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountInfo {

  String bank;
  String accountId;

  public AccountInfo(Account account) {
    this.bank = "WeBank";
    this.accountId = String.valueOf(account.getAccountId());
  }

  public AccountInfo(ExternalAccount account) {
    this.bank = account.getBankName();
    this.accountId = account.getAccountNumber();
  }

}
