package practice.adaptor.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import practice.common.dto.CommonResponse;
import practice.common.dto.PaymentRequest;
import practice.common.dto.PaymentResponse;
import practice.common.idempotency.IdempotencyCheck;
import practice.payment.application.in.PaymentFacade;

@RestController
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentFacade paymentFacade;

  @IdempotencyCheck("#idempotencyKey")
  @PostMapping("/payments")
  public CommonResponse<PaymentResponse> processPayment(
      @RequestHeader("Idempotency-Key") String idempotencyKey,
      @Valid @RequestBody PaymentRequest paymentRequest) {

    PaymentResponse result = paymentFacade.pay(paymentRequest);

    return CommonResponse.<PaymentResponse>builder()
        .detail(result)
        .build();
  }

}
