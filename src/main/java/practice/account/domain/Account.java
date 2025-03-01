package practice.account.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

  Long accountId;
  Long userId;
  String accountNumber;

  AccountType type;
  BigDecimal balance;

  static final BigDecimal DAILY_WITHDRAWAL_LIMIT = BigDecimal.valueOf(3_000_000);
  BigDecimal todayWithdraw;

  @Builder.Default
  TransactionHistoryList transactionHistoryList = new TransactionHistoryList(new ArrayList<>());

  public boolean canNotWithdrawNow(BigDecimal amount) {
    return !(AccountType.MAIN.equals(type)
        && balance.compareTo(amount) >= 0);
  }

  public boolean isExternalWithdrawLimitExceeded() {
    return !(AccountType.MAIN.equals(type)
        && todayWithdraw.compareTo(DAILY_WITHDRAWAL_LIMIT) < 0);
  }

  public BigDecimal calculateRequiredAmount(BigDecimal amount){
    return amount.subtract(balance);
  }

  public void withdraw(BigDecimal amount, Account targetAccount) {
    if(canNotWithdrawNow(amount)) {
      return;
    }

    balance = balance.subtract(amount);
    transactionHistoryList.add(TransactionType.WITHDRAW, amount, new AccountInfo(targetAccount));
  }

  public void withdraw(BigDecimal amount, Long paymentId) {
    if(canNotWithdrawNow(amount)) {
      return;
    }

    balance = balance.subtract(amount);
    transactionHistoryList.add(TransactionType.PAYMENT, amount, paymentId);
  }

  public void deposit(BigDecimal amount, Account sourceAccount) {
    balance = balance.add(amount);
    transactionHistoryList.add(TransactionType.DEPOSIT, amount, new AccountInfo(sourceAccount));
  }

  public void deposit(TransactionHistory transactionHistory) {
    balance = balance.add(transactionHistory.getAmount());
    transactionHistory.setToDeposit();
    transactionHistoryList.add(transactionHistory);
  }




}
