package practice.adaptor.out

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import practice.account.application.out.SaveTransactionPort
import practice.account.domain.ExternalAccount
import practice.account.domain.TransactionHistory
import practice.account.domain.TransactionType
import practice.adaptor.out.entity.AccountEntity
import practice.adaptor.out.entity.TransactionEntity
import java.math.BigDecimal
import java.time.LocalDate

@Component
class TransactionPersistenceAdaptor(
    private val transactionJpaRepository: TransactionJpaRepository,
    private val todayExternalWithdrawRepository: TodayExternalWithdrawRepository,
    private val accountMapper: AccountMapper
) : SaveTransactionPort {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun saveExternalDeposit(
        externalAccount: ExternalAccount,
        accountId: Long,
        amount: BigDecimal
    ): TransactionHistory {
        val transaction = transactionJpaRepository.save(
            getTransactionEntity(externalAccount, accountId, amount)
        )
        todayExternalWithdrawRepository.increment(accountId, amount)
        return accountMapper.toDomain(transaction)
    }

    override fun rollbackExternalWithdraw(accountId: Long, amount: BigDecimal) {
        todayExternalWithdrawRepository.decrement(accountId, amount)
    }

    private fun getTransactionEntity(
        externalAccount: ExternalAccount,
        accountId: Long,
        amount: BigDecimal
    ): TransactionEntity {

        return TransactionEntity(
            createdDate = LocalDate.now(),
            account = AccountEntity(accountId),
            transactionType = TransactionType.DEPOSIT_PENDING.name,
            amount = amount,
            counterPartyBank = externalAccount.bankName,
            counterPartyAccountId = externalAccount.accountNumber,
        )
    }
}
