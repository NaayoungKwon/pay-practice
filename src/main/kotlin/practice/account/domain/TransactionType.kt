package practice.account.domain

import lombok.Getter

@Getter
enum class TransactionType {
    DEPOSIT,
    WITHDRAW,
    PAYMENT,
    DEPOSIT_PENDING
}
