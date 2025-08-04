package practice.account.application.out

import practice.account.domain.Account
import practice.account.domain.AccountType
import practice.account.domain.ExternalAccount

interface RegisterAccountPort {
    fun createAccount(userId: Long, accountType: AccountType): Account
    fun registerExternalAccount(userId: Long, bankCode: String, accountNumber: String): ExternalAccount
}
