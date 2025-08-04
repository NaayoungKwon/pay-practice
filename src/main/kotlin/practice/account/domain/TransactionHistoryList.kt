package practice.account.domain

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class TransactionHistoryList(
    var items: MutableList<TransactionHistory>
) {

    constructor() : this(mutableListOf())

    fun canWithdraw(money: BigDecimal): Boolean {
        val today = LocalDate.now()
        val totalWithdrawal = items.stream()
            .filter { transactionHistory: TransactionHistory -> TransactionType.WITHDRAW == transactionHistory.type }
            .filter { transactionHistory: TransactionHistory -> today == transactionHistory.transactionDate() }
            .map { obj: TransactionHistory -> obj.amount }
            .reduce(BigDecimal.ZERO) { obj: BigDecimal?, augend: BigDecimal? -> obj?.add(augend) }
        return totalWithdrawal?.add(money)?.compareTo(DAILY_WITHDRAWAL_LIMIT)!! > 0
    }

    fun add(type: TransactionType, amount: BigDecimal, accountInfo: AccountInfo) {
        items.add(
            TransactionHistory(
                transactionDateTime = LocalDateTime.now(),
                type = type,
                amount = amount,
                counterPartyBank = accountInfo.bank,
                counterPartyAccountId = accountInfo.accountId
            )
        )
    }

    fun add(type: TransactionType, amount: BigDecimal, id: Long) {
        items.add(
            TransactionHistory(
                transactionDateTime = LocalDateTime.now(),
                type = type,
                amount = amount,
                counterPartyAccountId = if (TransactionType.PAYMENT == type) null else id.toString(),
                paymentId = if (TransactionType.PAYMENT == type) id else null
            )
        )
    }

    fun add(transactionHistory: TransactionHistory) {
        items.add(transactionHistory)
    }

    companion object {
        private val DAILY_WITHDRAWAL_LIMIT: BigDecimal = BigDecimal.valueOf(3000000)
    }
}
