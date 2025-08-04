package practice.adaptor.out.api

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import practice.account.application.out.ExternalBankPort
import practice.account.application.out.SaveTransactionPort
import practice.account.domain.ExternalAccount
import practice.account.domain.TransactionHistory
import practice.common.exception.ExternalAccountErrorException
import practice.common.util.RandomGenerator
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.math.BigDecimal
import java.time.Duration

private val log = KotlinLogging.logger { }

@Component
class HanaBankApi(
    private val saveTransactionPort: SaveTransactionPort,
    private val randomGenerator: RandomGenerator
) : ExternalBankPort {

    override fun withdraw(
        externalAccount: ExternalAccount, accountId: Long,
        amount: BigDecimal
    ): TransactionHistory {
        val transactionHistory = saveTransactionPort.saveExternalDeposit(externalAccount, accountId, amount)

        try {
            log.info { "[External] Deposit from ${externalAccount.bankName} : ${amount}원" }
            requestWithRetry(amount).block()
        } catch (e: Exception) {
            saveTransactionPort.rollbackExternalWithdraw(accountId, amount)
            throw ExternalAccountErrorException(e.message)
        }

        return transactionHistory
    }

    private fun requestWithRetry(amount: BigDecimal): Mono<BigDecimal> {
        return Mono.delay(getRandomDelay())
            .then(generateMockResponse(amount))
            .retryWhen(
                Retry.backoff(RETRY_COUNT.toLong(), Duration.ofMillis(10))
                    .filter { obj: Throwable? -> RuntimeException::class.isInstance(obj) }
                    .maxBackoff(Duration.ofSeconds(2))
            )
    }

    private fun getRandomDelay(): Duration {
        val delay = randomGenerator.nextInt(10, 100)
        return Duration.ofMillis(delay.toLong())
    }

    private fun generateMockResponse(amount: BigDecimal): Mono<BigDecimal> {
        return Mono.defer<BigDecimal> {
            val status = getRandomHttpStatus()
            log.info { "Generated HTTP Status: $status" }

            if (status.is4xxClientError || status.is5xxServerError) {
                throw RuntimeException("HTTP Error: $status")
            }
            Mono.just<BigDecimal>(amount)
        }
    }

    private fun getRandomHttpStatus(): HttpStatus {
        val statusCodes = listOf(200, 200, 200, 200, 404, 500)
        return HttpStatus.valueOf(
            statusCodes[randomGenerator.nextInt(0, statusCodes.size)]
        )
    }

    companion object {
        private const val RETRY_COUNT = 3
    }
}
