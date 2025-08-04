package practice.user.application.out

import practice.user.domain.User

interface RegisterUserPort {
    fun register(name: String, email: String): User
}
