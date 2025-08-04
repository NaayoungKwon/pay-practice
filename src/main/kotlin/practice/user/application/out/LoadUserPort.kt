package practice.user.application.out

interface LoadUserPort {
    fun hasUser(name: String, email: String): Boolean
}
