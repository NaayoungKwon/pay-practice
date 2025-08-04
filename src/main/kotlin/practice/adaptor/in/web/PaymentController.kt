package practice.adaptor.`in`.web

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import practice.common.dto.CommonResponse
import practice.common.dto.PaymentRequest
import practice.common.dto.PaymentResponse
import practice.common.idempotency.IdempotencyCheck
import practice.payment.application.`in`.PaymentFacade

@RestController
class PaymentController(
    private val paymentFacade: PaymentFacade
) {

    @IdempotencyCheck("#idempotencyKey")
    @PostMapping("/payments")
    fun processPayment(
        @RequestHeader("Idempotency-Key") idempotencyKey: String?,
        @RequestBody paymentRequest: @Valid PaymentRequest
    ): CommonResponse<PaymentResponse> {
        val result: PaymentResponse = paymentFacade.pay(paymentRequest)

        return CommonResponse<PaymentResponse>(
            detail = result
        )
    }
}
