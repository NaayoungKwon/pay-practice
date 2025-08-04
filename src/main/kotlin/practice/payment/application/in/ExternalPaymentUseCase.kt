package practice.payment.application.`in`

import practice.payment.domain.Payment

interface ExternalPaymentUseCase {
    fun prepare(payment: Payment): Payment

    fun success(payment: Payment): Payment
}
