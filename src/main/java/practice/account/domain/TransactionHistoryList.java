package practice.account.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TransactionHistoryList(List<TransactionHistory> transactionHistoryList) {

  private static final BigDecimal DAILY_WITHDRAWAL_LIMIT = BigDecimal.valueOf(3_000_000);

  public boolean canWithdraw(BigDecimal money) {
    LocalDate today = LocalDate.now();
    BigDecimal totalWithdrawal = transactionHistoryList.stream()
        .filter(transactionHistory ->TransactionType.WITHDRAW.equals(transactionHistory.getType()))
        .filter(transactionHistory -> today.equals(transactionHistory.getTransactionDate()))
        .map(TransactionHistory::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return totalWithdrawal.add(money).compareTo(DAILY_WITHDRAWAL_LIMIT) > 0;
  }

  public void add(TransactionType type, BigDecimal amount, AccountInfo accountInfo) {
    transactionHistoryList.add(TransactionHistory.builder()
        .transactionDateTime(LocalDateTime.now())
        .type(type)
        .amount(amount)
        .opponentAccount(accountInfo.getAccountInfo())
        .build());
  }
}
