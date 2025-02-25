package practice.account.domain;

import lombok.Getter;

@Getter
public enum TransactionType {
  DEPOSIT,
  WITHDRAW,
  PAYMENT,
  DEPOSIT_PENDING
  ;
}
