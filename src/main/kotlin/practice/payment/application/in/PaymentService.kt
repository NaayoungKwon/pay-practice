package practice.payment.application.`in`

import org.springframework.stereotype.Service
import practice.payment.application.out.SavePaymentPort
import practice.payment.domain.Payment

@Service
class PaymentService(
    private val savePaymentPort: SavePaymentPort
) : ExternalPaymentUseCase {

    override fun prepare(payment: Payment): Payment {
        return savePaymentPort.saveToPrepare(payment)
    }

    override fun success(payment: Payment): Payment {
        return savePaymentPort.saveToSuccess(payment)
    }
}
