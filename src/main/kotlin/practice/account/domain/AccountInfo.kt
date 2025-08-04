package practice.account.domain

class AccountInfo(
    val bank: String?,
    val accountId: String?
) {

    constructor(account: Account) : this("WeBank", account.accountId.toString())

    constructor(account: ExternalAccount) : this(account.bankName, account.accountNumber)
}
