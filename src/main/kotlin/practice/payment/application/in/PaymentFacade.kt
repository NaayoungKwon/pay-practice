package practice.payment.application.`in`

import org.springframework.stereotype.Service
import practice.account.application.`in`.TransactionUseCase
import practice.common.dto.PaymentRequest
import practice.common.dto.PaymentResponse
import practice.payment.domain.Payment

@Service
class PaymentFacade(
    private val externalPaymentUseCase: ExternalPaymentUseCase,
    private val transactionUseCase: TransactionUseCase
) {

    fun pay(paymentRequest: PaymentRequest): PaymentResponse {
        val payment: Payment = externalPaymentUseCase.prepare(paymentRequest.toDomain())
        transactionUseCase.withdrawForPay(
            paymentRequest.userId,
            paymentRequest.totalPayAmount,
            payment.paymentId!!
        )
        val result: Payment = externalPaymentUseCase.success(payment)
        return PaymentResponse.of(result)
    }
}
