package practice.common.dto

import com.fasterxml.jackson.annotation.JsonFormat
import practice.payment.domain.Payment
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentResponse(
    val paymentId: Long,
    val partnerPayKey: String,
    val totalPayAmount: BigDecimal,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val paymentDateTime: LocalDateTime
) {


    companion object {
        fun of(payment: Payment): PaymentResponse {
            return PaymentResponse(
                paymentId = payment.paymentId!!,
                partnerPayKey = payment.partnerPayKey,
                totalPayAmount = payment.totalPayAmount,
                paymentDateTime = payment.paymentDateTime!!
            )
        }
    }
}
