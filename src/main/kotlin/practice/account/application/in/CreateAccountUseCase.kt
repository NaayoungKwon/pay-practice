package practice.account.application.`in`

import practice.account.domain.Account
import practice.account.domain.ExternalAccount

interface CreateAccountUseCase {

    fun createMainAccount(userId: Long): Account
    fun createSavingAccount(userId: Long): Account
    fun registerExternalAccount(userId: Long, bankCode: String, accountNumber: String): ExternalAccount

}