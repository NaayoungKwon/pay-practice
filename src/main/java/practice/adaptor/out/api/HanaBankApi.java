package practice.adaptor.out.api;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import practice.account.application.out.ExternalBankPort;
import practice.account.domain.ExternalAccount;

@Slf4j
@Component
@RequiredArgsConstructor
public class HanaBankApi implements ExternalBankPort {

  @Override
  public BigDecimal getBalance(ExternalAccount externalAccount, BigDecimal amount) {
    log.debug("[External] Deposit from {} : {}원", externalAccount.getBankName(), amount);
    return amount;
  }
}
