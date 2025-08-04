package practice.adaptor.`in`.web

import org.springframework.web.bind.annotation.*
import practice.account.application.`in`.CreateAccountUseCase
import practice.account.application.`in`.TransactionUseCase
import practice.common.dto.CreateAccountRequest
import practice.common.dto.TransactionRequest

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val createAccountUseCase: CreateAccountUseCase,
    private val transactionUseCase: TransactionUseCase
) {

    @PostMapping("/saving")
    fun createAccount(@RequestBody createAccountRequest: CreateAccountRequest) {
        createAccountUseCase.createSavingAccount(createAccountRequest.userId)
    }

    @PatchMapping("/saving/deposit")
    fun depositToSavingAccount(@RequestBody transactionRequest: TransactionRequest) {
        transactionUseCase.depositToSavingAccount(transactionRequest.userId, transactionRequest.amount)
    }
}