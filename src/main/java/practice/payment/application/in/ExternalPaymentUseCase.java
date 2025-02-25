package practice.payment.application.in;

import practice.payment.domain.Payment;

public interface ExternalPaymentUseCase {

  Payment prepare(Payment payment);

  Payment success(Payment payment);

}
