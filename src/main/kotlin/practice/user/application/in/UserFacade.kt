package practice.user.application.`in`

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import practice.account.application.`in`.CreateAccountUseCase
import practice.common.dto.UserRequest
import practice.user.domain.User

@Service
class UserFacade(
    private val createAccountUseCase: CreateAccountUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) {

    @Transactional
    fun createAccount(userRequest: UserRequest) {
        val name: String = userRequest.name
        val email: String = userRequest.email

        if (registerUserUseCase.alreadyExists(name, email)) {
            return
        }

        val user: User = registerUserUseCase.register(name, email)
        createAccountUseCase.createMainAccount(user.id)
        createAccountUseCase.registerExternalAccount(
            user.id,
            userRequest.externalBank,
            userRequest.externalAccountNumber
        )
    }
}
