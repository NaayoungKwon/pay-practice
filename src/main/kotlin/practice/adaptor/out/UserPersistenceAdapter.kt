package practice.adaptor.out

import org.springframework.stereotype.Component
import practice.adaptor.out.entity.UserEntity
import practice.user.application.out.LoadUserPort
import practice.user.application.out.RegisterUserPort
import practice.user.domain.User

@Component
class UserPersistenceAdapter(
    private val userMapper: UserMapper,
    private val userJpaRepository: UserJpaRepository
) : LoadUserPort, RegisterUserPort {

    override fun hasUser(name: String, email: String): Boolean {
        return userJpaRepository.findByNameAndEmail(name, email).isPresent
    }

    override fun register(name: String, email: String): User {
        val userEntity = UserEntity(name = name, email = email)
        return userMapper.toDomain(userJpaRepository.save(userEntity))
    }
}
