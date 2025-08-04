package practice.payment.application.out

import practice.payment.domain.Payment

interface SavePaymentPort {
    fun saveToPrepare(payment: Payment): Payment

    fun saveToSuccess(payment: Payment): Payment
}
