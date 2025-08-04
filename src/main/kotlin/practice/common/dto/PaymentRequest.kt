package practice.common.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import practice.payment.domain.Payment
import practice.payment.domain.Product
import practice.user.domain.User
import java.math.BigDecimal

data class PaymentRequest(
    @field:NotNull(message = "User ID cannot be null")
    var userId: Long,

    @field:NotNull(message = "Partner ID cannot be null")
    var partnerId: Long,

    @field:NotNull(message = "Partner Pay Key cannot be null")
    var partnerPayKey: String,

    @field:NotNull(message = "Product items cannot be null")
    @field:Size(min = 1, message = "At least one product item is required")
    var productItems: MutableList<Product>,

    @field:NotNull(message = "Total pay amount cannot be null")
    var totalPayAmount: BigDecimal
) {

    fun toDomain(): Payment {
        return Payment(
            user = User(id = userId),
            partnerId = partnerId,
            partnerPayKey = partnerPayKey,
            productItems = productItems,
            totalPayAmount = totalPayAmount
        )
    }
}