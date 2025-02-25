package practice.payment.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import practice.user.domain.User;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

  User user;
  Long partnerId;
  Long paymentId;
  String partnerPayKey;
  List<Product> productItems;
  BigDecimal totalPayAmount;
  LocalDateTime paymentDateTime;

}
