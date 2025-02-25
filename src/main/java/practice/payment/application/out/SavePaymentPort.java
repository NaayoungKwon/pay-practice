package practice.payment.application.out;

import practice.payment.domain.Payment;

public interface SavePaymentPort {

  Payment saveToPrepare(Payment payment);
  Payment saveToSuccess(Payment payment);
}
