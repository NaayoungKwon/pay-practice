package practice.adaptor.out.api;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import practice.account.application.out.ExternalBankPort;

@Component
@RequiredArgsConstructor
public class HanaBankApi implements ExternalBankPort {

  @Override
  public BigDecimal getBalance(Long userId, BigDecimal amount) {
    return amount;
  }
}
