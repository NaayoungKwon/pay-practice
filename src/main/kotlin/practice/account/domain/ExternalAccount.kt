package practice.account.domain

data class ExternalAccount(
    var userId: Long? = null,
    var bankName: String? = null,
    var accountNumber: String? = null
)