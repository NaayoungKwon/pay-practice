package practice.account.domain

import java.math.BigDecimal

class Account(
    var accountId: Long? = null,
    var userId: Long? = null,
    var accountNumber: String? = null,
    var type: AccountType? = null,
    var balance: BigDecimal? = null,
    var todayWithdraw: BigDecimal? = null,
    var transactionHistoryList: TransactionHistoryList = TransactionHistoryList()
) {

    companion object {
        private val DAILY_WITHDRAWAL_LIMIT = BigDecimal.valueOf(3000000)
    }

    fun canNotWithdrawNow(amount: BigDecimal?): Boolean {
        return !(AccountType.MAIN == type && balance!! >= amount)
    }

    fun isExternalWithdrawLimitExceeded(): Boolean {
        return !(AccountType.MAIN == type && todayWithdraw!! < DAILY_WITHDRAWAL_LIMIT)
    }

    fun calculateRequiredAmount(amount: BigDecimal): BigDecimal {
        return amount.subtract(balance)
    }

    fun withdraw(amount: BigDecimal, targetAccount: Account) {
        if (canNotWithdrawNow(amount)) {
            return
        }

        balance = balance!!.subtract(amount)
        transactionHistoryList.add(TransactionType.WITHDRAW, amount, AccountInfo(targetAccount))
    }

    fun withdraw(amount: BigDecimal, paymentId: Long) {
        if (canNotWithdrawNow(amount)) {
            return
        }

        balance = balance!!.subtract(amount)
        transactionHistoryList.add(TransactionType.PAYMENT, amount, paymentId)
    }

    fun deposit(amount: BigDecimal, sourceAccount: Account) {
        balance = balance!!.add(amount)
        transactionHistoryList.add(TransactionType.DEPOSIT, amount, AccountInfo(sourceAccount))
    }

    fun deposit(transactionHistory: TransactionHistory) {
        balance = balance!!.add(transactionHistory.amount)
        transactionHistory.setToDeposit()
        transactionHistoryList.add(transactionHistory)
    }
}