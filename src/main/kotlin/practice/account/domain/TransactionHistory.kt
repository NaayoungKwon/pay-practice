package practice.account.domain

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class TransactionHistory(
    var id: Long? = null,
    var transactionDateTime: LocalDateTime? = null,
    var type: TransactionType? = null,
    var amount: BigDecimal? = null,
    var counterPartyBank: String? = null,
    var counterPartyAccountId: String? = null,
    var paymentId: Long? = null
) {

    fun transactionDate(): LocalDate? {
        return transactionDateTime?.toLocalDate()
    }

    fun setToDeposit() {
        this.type = TransactionType.DEPOSIT
    }
}
