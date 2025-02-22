package practice.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountInfo {

  String bank;
  Long accountId;
  String externalAccountNumber;

  public AccountInfo(Account account) {
    this.bank = "WeBank";
    this.accountId = account.getAccountId();
  }

  public AccountInfo(ExternalAccount account) {
    this.bank = account.getBankName();
    this.externalAccountNumber = account.getAccountNumber();
  }

  public String getAccountInfo() {
    if(accountId != null) {
      return bank + "/" + accountId;
    }
    return bank + "/" + externalAccountNumber;
  }

}
