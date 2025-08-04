package practice.adaptor.out

import org.springframework.data.jpa.repository.JpaRepository
import practice.adaptor.out.entity.UserEntity
import java.util.*

interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByNameAndEmail(name: String, email: String): Optional<UserEntity>
}
