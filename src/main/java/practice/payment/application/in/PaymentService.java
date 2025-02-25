package practice.payment.application.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.payment.application.out.SavePaymentPort;
import practice.payment.domain.Payment;

@Service
@RequiredArgsConstructor
public class PaymentService implements ExternalPaymentUseCase {

  private final SavePaymentPort savePaymentPort;

  @Override
  public Payment prepare(Payment payment) {
    return savePaymentPort.saveToPrepare(payment);
  }

  @Override
  public Payment success(Payment payment) {
    return savePaymentPort.saveToSuccess(payment);
  }
}
