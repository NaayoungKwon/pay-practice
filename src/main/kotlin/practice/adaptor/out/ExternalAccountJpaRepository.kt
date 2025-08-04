package practice.adaptor.out

import org.springframework.data.jpa.repository.JpaRepository
import practice.adaptor.out.entity.ExternalAccountEntity
import practice.adaptor.out.entity.UserEntity

interface ExternalAccountJpaRepository : JpaRepository<ExternalAccountEntity, Long> {
    fun findByUser(user: UserEntity): ExternalAccountEntity?
}
