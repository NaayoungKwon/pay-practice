package practice.common.dto

import java.math.BigDecimal

data class TransactionRequest(
    val userId: Long,
    val amount: BigDecimal
)