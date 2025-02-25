package practice.adaptor.out.api;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import practice.account.application.out.ExternalBankPort;
import practice.account.application.out.SaveTransactionPort;
import practice.account.domain.ExternalAccount;
import practice.account.domain.TransactionHistory;
import practice.common.util.RandomGenerator;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Component
@RequiredArgsConstructor
public class HanaBankApi implements ExternalBankPort {

  private final SaveTransactionPort saveTransactionPort;
  private final RandomGenerator randomGenerator;
  private static final Integer RETRY_COUNT = 3;

  @Override
  public TransactionHistory withdraw(ExternalAccount externalAccount, Long accountId,
      BigDecimal amount) {
    TransactionHistory transactionHistory = saveTransactionPort.saveExternalDeposit(externalAccount,
        accountId, amount);

    log.info("[External] Deposit from {} : {}원", externalAccount.getBankName(), amount);
    requestWithRetry(amount).block();

    return transactionHistory;
  }

  private Mono<BigDecimal> requestWithRetry(BigDecimal amount) {
    return Mono.delay(getRandomDelay())
        .then(generateMockResponse(amount))
        .retryWhen(Retry.backoff(RETRY_COUNT, Duration.ofMillis(10))
            .filter(RuntimeException.class::isInstance)
            .maxBackoff(Duration.ofSeconds(2))
        );
  }

  private Duration getRandomDelay() {
    int delay = randomGenerator.nextInt(10, 100);
    return Duration.ofMillis(delay);
  }

  private Mono<BigDecimal> generateMockResponse(BigDecimal amount) {
    return Mono.defer(() -> {
      HttpStatus status = getRandomHttpStatus();
      log.info("Http Response Status: " + status);

      if (status.is4xxClientError() || status.is5xxServerError()) {
        throw new RuntimeException("HTTP Error: " + status);
      }
      return Mono.just(amount);
    });
  }

  private HttpStatus getRandomHttpStatus() {
    List<Integer> statusCodes = List.of(200, 200, 200, 200, 404, 500);
    return HttpStatus.valueOf(statusCodes.get(randomGenerator.nextInt(0, statusCodes.size())));
  }
}
