package practice.account.application.`in`

import java.math.BigDecimal

interface TransactionUseCase {

    fun depositToMainAccount(userId: Long, amount: BigDecimal)
    fun depositToSavingAccount(userId: Long, amount: BigDecimal)
    fun withdrawForPay(userId: Long, amount: BigDecimal, paymentId: Long)
}