package practice.payment.application.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.account.application.in.TransactionUseCase;
import practice.common.dto.PaymentRequest;
import practice.common.dto.PaymentResponse;
import practice.payment.domain.Payment;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

  private final ExternalPaymentUseCase externalPaymentUseCase;
  private final TransactionUseCase transactionUseCase;

  public PaymentResponse pay(PaymentRequest paymentRequest) {
    Payment payment = externalPaymentUseCase.prepare(paymentRequest.toDomain());
    transactionUseCase.withdrawForPay(paymentRequest.getUserId(), paymentRequest.getTotalPayAmount(), payment.getPaymentId());
    Payment result = externalPaymentUseCase.success(payment);
    return PaymentResponse.of(result);
  }
}
