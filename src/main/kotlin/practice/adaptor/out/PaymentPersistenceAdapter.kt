package practice.adaptor.out

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import practice.payment.application.out.SavePaymentPort
import practice.payment.domain.Payment

@Component
class PaymentPersistenceAdapter(
    private val paymentMapper: PaymentMapper,
    private val paymentJpaRepository: PaymentJpaRepository
) : SavePaymentPort {

    override fun saveToPrepare(payment: Payment): Payment {
        val paymentEntity = paymentMapper.toEntityPrepared(payment)
        paymentJpaRepository.save(paymentEntity)
        return paymentMapper.toDomain(paymentEntity)
    }

    @Transactional
    override fun saveToSuccess(payment: Payment): Payment {
        paymentJpaRepository.updateStatus("DONE", payment.paymentId!!)
        val paymentEntity = paymentJpaRepository.findById(payment.paymentId)
            .orElseThrow()
        return paymentMapper.toDomain(paymentEntity)
    }
}
