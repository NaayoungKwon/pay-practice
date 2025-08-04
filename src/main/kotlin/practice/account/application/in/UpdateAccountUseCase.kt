package practice.account.application.`in`

import practice.account.domain.Account

interface UpdateAccountUseCase {

    fun updateAccount(account: Account)
    fun updateAccount(accounts: List<Account>)
}