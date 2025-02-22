package practice.account.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionHistory {

  LocalDateTime transactionDateTime;
  TransactionType type;
  BigDecimal amount;

  public LocalDate getTransactionDate() {
    return transactionDateTime.toLocalDate();
  }

}
