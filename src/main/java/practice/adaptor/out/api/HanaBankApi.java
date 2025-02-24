package practice.adaptor.out.api;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import practice.account.application.out.ExternalBankPort;
import practice.account.application.out.SaveTransactionPort;
import practice.account.domain.ExternalAccount;
import practice.account.domain.TransactionHistory;

@Slf4j
@Component
@RequiredArgsConstructor
public class HanaBankApi implements ExternalBankPort {

  private final SaveTransactionPort saveTransactionPort;

  @Override
  public TransactionHistory withdraw(ExternalAccount externalAccount, Long accountId,
      BigDecimal amount) {
    TransactionHistory transactionHistory = saveTransactionPort.saveExternalDeposit(externalAccount, accountId,
        amount);
    log.debug("[External] Deposit from {} : {}원", externalAccount.getBankName(), amount);
    return transactionHistory;
  }
}
