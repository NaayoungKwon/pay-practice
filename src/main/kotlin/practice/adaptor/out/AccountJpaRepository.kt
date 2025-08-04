package practice.adaptor.out

import org.springframework.data.jpa.repository.JpaRepository
import practice.adaptor.out.entity.AccountEntity
import practice.adaptor.out.entity.UserEntity

interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {
    fun findByUserAndType(userEntity: UserEntity, type: String): AccountEntity
}
