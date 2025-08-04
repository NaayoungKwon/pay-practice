package practice.account.application.out

import practice.account.domain.Account
import practice.account.domain.AccountType
import practice.account.domain.ExternalAccount

interface LoadAccountPort {
    fun findAccount(userId: Long, accountType: AccountType): Account
    fun findMainAccountWithTodayWithdraw(userId: Long): Account
    fun findExternalAccount(userId: Long): ExternalAccount?
}
