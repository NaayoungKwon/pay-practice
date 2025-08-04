package practice.payment.domain

import practice.user.domain.User
import java.math.BigDecimal
import java.time.LocalDateTime

class Payment(
    val user: User,
    val partnerId: Long,
    val paymentId: Long? = null,
    val partnerPayKey: String,
    val productItems: List<Product>,
    val totalPayAmount: BigDecimal,
    val paymentDateTime: LocalDateTime? = null,
)
