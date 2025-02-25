package practice.account.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AccountTest {


  @ParameterizedTest
  @ValueSource(ints = {100, 300, 1000})
  @DisplayName("현재 계좌 잔액으로 출금 가능한지 확인")
  void canWithdrawNow(Integer withdrawAmount) {
    // given
    Account account = Account.builder()
        .type(AccountType.MAIN)
        .balance(BigDecimal.valueOf(1000)).build();

    // when
    boolean result = account.canWithdrawNow(BigDecimal.valueOf(withdrawAmount));

    // then
    assertTrue(result);
  }

  @ParameterizedTest
  @ValueSource(ints = {1001, 3000, 10000})
  @DisplayName("현재 계좌 잔액으로 출금 불가능한지 확인")
  void canNotWithdrawNow(Integer withdrawAmount) {
    // given
    Account account = Account.builder()
        .type(AccountType.MAIN)
        .balance(BigDecimal.valueOf(1000)).build();

    // when
    boolean result = account.canWithdrawNow(BigDecimal.valueOf(withdrawAmount));

    // then
    assertFalse(result);
  }


  @Test
  @DisplayName("메인 계좌가 아니면 출금 불가능하다.")
  void canNotWithdrawSaving() {
    // given
    Account account = Account.builder()
        .type(AccountType.SAVINGS)
        .balance(BigDecimal.valueOf(1000)).build();

    // when
    boolean result = account.canWithdrawNow(BigDecimal.valueOf(500));

    // then
    assertFalse(result);
  }

  @ParameterizedTest
  @ValueSource(ints = {10000, 200000, 2_999_999})
  @DisplayName("일일 외부 계좌 출금 한도 넘지 않음을 확인")
  void overTodaysWithdrawLimit(Integer todayWithdraw) {
    // given
    Account account = Account.builder()
        .type(AccountType.MAIN)
        .todayWithdraw(BigDecimal.valueOf(todayWithdraw)).build();

    // when
    boolean result = account.canWithdrawToExternalAccount();

    // then
    assertTrue(result);
  }

  @Test
  @DisplayName("계좌 인출 확인 테스트")
  void withdraw() {
    // given
    Account account = Account.builder()
        .type(AccountType.MAIN)
        .balance(BigDecimal.valueOf(1000)).build();
    Account targetAccount = Account.builder()
        .type(AccountType.SAVINGS)
        .balance(BigDecimal.valueOf(1000)).build();

    // when
    account.withdraw(BigDecimal.valueOf(200), targetAccount);

    // then
    assertEquals(BigDecimal.valueOf(800), account.getBalance());
    assertEquals(1, account.getTransactionHistoryList().transactionHistoryList().size());
    assertEquals(TransactionType.WITHDRAW, account.getTransactionHistoryList().transactionHistoryList().get(0).getType());
  }

}
