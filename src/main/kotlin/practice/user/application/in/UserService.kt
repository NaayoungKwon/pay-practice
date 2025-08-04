package practice.user.application.`in`

import org.springframework.stereotype.Service
import practice.user.application.out.LoadUserPort
import practice.user.application.out.RegisterUserPort
import practice.user.domain.User

@Service
class UserService(
    private val loadUserPort: LoadUserPort,
    private val registerUserPort: RegisterUserPort
) : RegisterUserUseCase {


    override fun alreadyExists(name: String, email: String): Boolean {
        return loadUserPort.hasUser(name, email)
    }

    override fun register(name: String, email: String): User {
        return registerUserPort.register(name, email)
    }
}
