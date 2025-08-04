package practice.adaptor.out

import org.springframework.stereotype.Component
import practice.account.application.`in`.UpdateAccountUseCase
import practice.account.application.out.LoadAccountPort
import practice.account.application.out.RegisterAccountPort
import practice.account.domain.Account
import practice.account.domain.AccountType
import practice.account.domain.ExternalAccount
import practice.account.domain.TransactionType
import practice.adaptor.out.entity.AccountEntity
import practice.adaptor.out.entity.ExternalAccountEntity
import practice.adaptor.out.entity.TransactionEntity
import practice.adaptor.out.entity.UserEntity
import practice.common.util.RandomGenerator
import java.math.BigDecimal

@Component
class AccountPersistenceAdapter(
    private val accountMapper: AccountMapper,
    private val accountJpaRepository: AccountJpaRepository,
    private val externalAccountJpaRepository: ExternalAccountJpaRepository,
    private val transactionJpaRepository: TransactionJpaRepository,
    private val todayExternalWithdrawRepository: TodayExternalWithdrawRepository,
    private val randomGenerator: RandomGenerator,
) : LoadAccountPort, RegisterAccountPort, UpdateAccountUseCase {

    override fun createAccount(userId: Long, accountType: AccountType): Account {
        val accountEntity = AccountEntity(
            type = accountType.name,
            user = UserEntity(userId),
            balance = BigDecimal.ZERO,
            accountNumber = randomGenerator.uuid,
        )
        return accountMapper.toDomain(accountJpaRepository.save<AccountEntity>(accountEntity))
    }

    override fun registerExternalAccount(
        userId: Long,
        bankName: String,
        accountNumber: String
    ): ExternalAccount {
        val externalAccountEntity = ExternalAccountEntity(
            bank = bankName,
            accountNumber = accountNumber,
            user = UserEntity(userId)
        )
        return accountMapper.toDomain(externalAccountJpaRepository.save<ExternalAccountEntity>(externalAccountEntity))
    }

    override fun findAccount(userId: Long, accountType: AccountType): Account {
        return accountMapper.toDomain(
            accountJpaRepository.findByUserAndType(UserEntity(userId), accountType.name)
        )
    }

    override fun findMainAccountWithTodayWithdraw(userId: Long): Account {
        val accountEntity = accountJpaRepository.findByUserAndType(
            UserEntity(userId), AccountType.MAIN.name
        )
        val todayWithdrawal = BigDecimal.valueOf(
            todayExternalWithdrawRepository[accountEntity.id!!].toLong()
        )
        return accountMapper.toDomain(accountEntity, todayWithdrawal)
    }

    override fun findExternalAccount(userId: Long): ExternalAccount? {
        return externalAccountJpaRepository.findByUser(UserEntity(userId))
            ?.let { accountMapper.toDomain(it) }
    }

    override fun updateAccount(account: Account) {
        val accountEntity = accountMapper.toEntityAlone(account)
            .also { it.transactions.forEach { t: TransactionEntity -> t.account = it } }
        updateTransactionType(getUpdatedTransaction(accountEntity))
        accountEntity.transactions = getNewTransaction(accountEntity)
        accountJpaRepository.save(accountEntity)
    }

    override fun updateAccount(accounts: List<Account>) {
        val accountEntities = accounts
            .map { account: Account ->
                accountMapper.toEntityAlone(account)
                    .also { it.transactions.forEach { t: TransactionEntity -> t.account = it } }
            }
            .toList()

        val updatedTransactionEntities = mutableListOf<TransactionEntity>()
        for (accountEntity in accountEntities) {
            updatedTransactionEntities.addAll(getUpdatedTransaction(accountEntity))
            accountEntity.transactions = getNewTransaction(accountEntity)
        }
        accountJpaRepository.saveAll(accountEntities)
        updateTransactionType(updatedTransactionEntities)
    }

    private fun updateTransactionType(updatedTransactionEntities: List<TransactionEntity>) {
        updatedTransactionEntities
            .forEach(this::updateTransactionType)
    }

    private fun updateTransactionType(transactionEntity: TransactionEntity) {
        transactionEntity.id?.let {
            transactionJpaRepository.updateTransactionType(TransactionType.DEPOSIT.name, it)
        }
    }

    private fun getNewTransaction(accountEntity: AccountEntity): MutableList<TransactionEntity> {
        return accountEntity.transactions.stream()
            .filter { it.id == null }
            .toList()
    }

    private fun getUpdatedTransaction(accountEntity: AccountEntity): List<TransactionEntity> {
        return accountEntity.transactions
            .filter { it.id != null }
            .toList()
    }
}
