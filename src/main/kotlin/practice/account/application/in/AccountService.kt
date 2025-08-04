package practice.account.application.`in`

import org.springframework.stereotype.Service
import practice.account.application.out.ExternalBankPort
import practice.account.application.out.LoadAccountPort
import practice.account.application.out.RegisterAccountPort
import practice.account.domain.Account
import practice.account.domain.AccountType
import practice.account.domain.ExternalAccount
import practice.common.exception.ExternalAccountLimitExceededException
import practice.common.lock.DistributedLock
import java.math.BigDecimal

@Service
class AccountService(
    private val registerAccountPort: RegisterAccountPort,
    private val loadAccountPort: LoadAccountPort,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val externalBankPort: ExternalBankPort
) : CreateAccountUseCase, TransactionUseCase {


    override fun createMainAccount(userId: Long): Account {
        return registerAccountPort.createAccount(userId, AccountType.MAIN)
    }

    override fun createSavingAccount(userId: Long): Account {
        return registerAccountPort.createAccount(userId, AccountType.SAVINGS)
    }

    override fun registerExternalAccount(userId: Long, bankCode: String, accountNumber: String): ExternalAccount {
        return registerAccountPort.registerExternalAccount(userId, bankCode, accountNumber)
    }

    @DistributedLock(key = "ACCOUNT", value = "#userId")
    override fun depositToMainAccount(userId: Long, amount: BigDecimal) {
        val account = loadAccountPort.findAccount(userId, AccountType.MAIN)
        withdrawFromExternalAccount(account, amount)
        updateAccountUseCase.updateAccount(account)
    }

    @DistributedLock(key = "ACCOUNT", value = "#userId")
    override fun depositToSavingAccount(userId: Long, amount: BigDecimal) {
        val mainAccount = loadAccountPort.findMainAccountWithTodayWithdraw(userId)
        if (mainAccount.canNotWithdrawNow(amount) && mainAccount.isExternalWithdrawLimitExceeded()) {
            throw ExternalAccountLimitExceededException()
        } else if (mainAccount.canNotWithdrawNow(amount)) {
            withdrawFromExternalAccount(mainAccount, mainAccount.calculateRequiredAmount(amount))
        }

        val savingAccount = loadAccountPort.findAccount(userId, AccountType.SAVINGS)
        mainAccount.withdraw(amount, savingAccount)
        savingAccount.deposit(amount, mainAccount)
        updateAccountUseCase.updateAccount(listOf(mainAccount, savingAccount))
    }

    @DistributedLock(key = "ACCOUNT", value = "#userId")
    override fun withdrawForPay(userId: Long, amount: BigDecimal, paymentId: Long) {
        val account = loadAccountPort.findMainAccountWithTodayWithdraw(userId)
        if (account.canNotWithdrawNow(amount) && account.isExternalWithdrawLimitExceeded()) {
            throw ExternalAccountLimitExceededException()
        } else if (account.canNotWithdrawNow(amount)) {
            withdrawFromExternalAccount(account, account.calculateRequiredAmount(amount))
        }
        account.withdraw(amount, paymentId)
        updateAccountUseCase.updateAccount(account)
    }

    private fun withdrawFromExternalAccount(account: Account, amount: BigDecimal) {
        loadAccountPort.findExternalAccount(account.userId!!)
            ?.let { externalBankPort.withdraw(it, account.accountId!!, amount) }
            ?.let {
                account.deposit(it)
            }
    }
}