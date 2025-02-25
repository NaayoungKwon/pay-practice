package practice.adaptor.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import practice.payment.domain.Payment;
import practice.payment.domain.Product;
import practice.user.domain.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {

  @NotNull
  Long userId;
  @NotNull
  Long partnerId;
  @NotNull
  String partnerPayKey;

  @NotNull
  @Size(min = 1)
  List<Product> productItems;
  @NotNull
  BigDecimal totalPayAmount;

  public Payment toDomain() {
    return Payment.builder()
        .user(User.builder().id(userId).build())
        .partnerId(partnerId)
        .partnerPayKey(partnerPayKey)
        .productItems(productItems)
        .totalPayAmount(totalPayAmount)
        .build();
  }

}
