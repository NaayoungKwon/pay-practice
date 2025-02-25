package practice.adaptor.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import practice.payment.domain.Payment;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {

  Long paymentId;
  String partnerPayKey;
  BigDecimal totalPayAmount;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime paymentDateTime;

  public static PaymentResponse of(Payment payment) {
    return PaymentResponse.builder()
        .paymentId(payment.getPaymentId())
        .partnerPayKey(payment.getPartnerPayKey())
        .totalPayAmount(payment.getTotalPayAmount())
        .paymentDateTime(payment.getPaymentDateTime())
        .build();
  }
}
