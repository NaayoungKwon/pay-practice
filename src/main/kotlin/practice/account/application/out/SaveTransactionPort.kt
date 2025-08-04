package practice.account.application.out

import practice.account.domain.ExternalAccount
import practice.account.domain.TransactionHistory
import java.math.BigDecimal

interface SaveTransactionPort {
    fun saveExternalDeposit(
        externalAccount: ExternalAccount,
        accountId: Long,
        amount: BigDecimal
    ): TransactionHistory

    fun rollbackExternalWithdraw(accountId: Long, amount: BigDecimal)
}
