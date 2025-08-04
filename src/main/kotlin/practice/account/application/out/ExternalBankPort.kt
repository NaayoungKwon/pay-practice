package practice.account.application.out

import practice.account.domain.ExternalAccount
import practice.account.domain.TransactionHistory
import java.math.BigDecimal

interface ExternalBankPort {

    fun withdraw(externalAccount: ExternalAccount, accountId: Long, amount: BigDecimal): TransactionHistory
}
