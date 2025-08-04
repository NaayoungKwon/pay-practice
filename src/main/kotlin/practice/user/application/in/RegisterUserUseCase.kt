package practice.user.application.`in`

import practice.user.domain.User

interface RegisterUserUseCase {
    fun alreadyExists(name: String, email: String): Boolean

    fun register(name: String, email: String): User
}
