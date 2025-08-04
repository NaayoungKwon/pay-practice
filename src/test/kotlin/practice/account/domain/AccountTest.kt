package practice.account.domain

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class AccountTest {
    @ParameterizedTest
    @ValueSource(ints = [100, 300, 1000])
    @DisplayName("현재 계좌 잔액으로 출금 가능한지 확인")
    fun canWithdrawNow(withdrawAmount: Int) {
        // given
        val account = Account(
            type = AccountType.MAIN,
            balance = BigDecimal(1000)
        )

        // when
        val result = account.canNotWithdrawNow(BigDecimal.valueOf(withdrawAmount.toLong()))

        // then
        Assertions.assertFalse(result)
    }

    @ParameterizedTest
    @ValueSource(ints = [1001, 3000, 10000])
    @DisplayName("현재 계좌 잔액으로 출금 불가능한지 확인")
    fun canNotWithdrawNow(withdrawAmount: Int) {
        // given
        val account = Account(
            type = AccountType.MAIN,
            balance = BigDecimal(1000)
        )

        // when
        val result = account.canNotWithdrawNow(BigDecimal.valueOf(withdrawAmount.toLong()))

        // then
        Assertions.assertTrue(result)
    }


    @Test
    @DisplayName("메인 계좌가 아니면 출금 불가능하다.")
    fun canNotWithdrawSaving() {
        // given
        val account = Account(
            type = AccountType.SAVINGS,
            balance = BigDecimal(1000)
        )

        // when
        val result = account.canNotWithdrawNow(BigDecimal.valueOf(500))

        // then
        Assertions.assertTrue(result)
    }

    @ParameterizedTest
    @ValueSource(ints = [10000, 200000, 2999999])
    @DisplayName("일일 외부 계좌 출금 한도 넘지 않음을 확인")
    fun overTodaysWithdrawLimit(todayWithdraw: Int) {
        // given
        val account = Account(
            type = AccountType.MAIN,
            todayWithdraw = BigDecimal(todayWithdraw.toLong())
        )

        // when
        val result = account.isExternalWithdrawLimitExceeded()

        // then
        Assertions.assertFalse(result)
    }

    @Test
    @DisplayName("계좌 인출 확인 테스트")
    fun withdraw() {
        // given
        val account = Account(
            type = AccountType.MAIN,
            balance = BigDecimal(1000)
        )
        val targetAccount = Account(
            type = AccountType.SAVINGS,
            balance = BigDecimal(1000)
        )

        // when
        account.withdraw(BigDecimal.valueOf(200), targetAccount)

        // then
        Assertions.assertEquals(BigDecimal.valueOf(800), account.balance)
        Assertions.assertEquals(1, account.transactionHistoryList.items.size)
        Assertions.assertEquals(
            TransactionType.WITHDRAW,
            account.transactionHistoryList.items[0].type
        )
    }
}
