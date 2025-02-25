package practice.adaptor.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import practice.adaptor.out.entity.PaymentEntity;
import practice.payment.application.out.SavePaymentPort;
import practice.payment.domain.Payment;

@Component
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements SavePaymentPort {

  private final PaymentMapper paymentMapper;
  private final PaymentJpaRepository paymentJpaRepository;

  @Override
  public Payment saveToPrepare(Payment payment) {
    PaymentEntity paymentEntity = paymentMapper.toEntityPrepared(payment);
    paymentJpaRepository.save(paymentEntity);
    return paymentMapper.toDomain(paymentEntity);
  }

  @Override
  @Transactional
  public Payment saveToSuccess(Payment payment) {
    paymentJpaRepository.updateStatus("DONE", payment.getPaymentId());
    PaymentEntity paymentEntity = paymentJpaRepository.findById(payment.getPaymentId())
        .orElseThrow();
    return paymentMapper.toDomain(paymentEntity);
  }
}
